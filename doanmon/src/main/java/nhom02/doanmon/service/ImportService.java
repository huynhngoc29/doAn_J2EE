package nhom02.doanmon.service;

import nhom02.doanmon.dto.CommitResult;
import nhom02.doanmon.dto.ImportResult;
import nhom02.doanmon.dto.ProductImportDto;
import nhom02.doanmon.entity.Cake;
import nhom02.doanmon.repository.CakeRepository;
import nhom02.doanmon.util.ExcelReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ImportService {

    @Autowired
    private CakeRepository cakeRepository;

    @Autowired
    private ExcelReader excelReader;

    public ImportResult preview(MultipartFile file) {
        // Lấy tất cả sản phẩm (Cake)
        List<Cake> products = cakeRepository.findAll();
        
        // Dùng HashSet để check tồn tại (O(1)) theo yêu cầu
        Set<String> existingCodes = products.stream()
                .map(Cake::getProductCode)
                .filter(code -> code != null) // cần thiết nếu productCode có thể null do dữ liệu cũ
                .collect(Collectors.toSet());
                
        // Lấy oldQuantity của từng Cake
        Map<String, Integer> currentQuantities = products.stream()
                .filter(cake -> cake.getProductCode() != null)
                .collect(Collectors.toMap(Cake::getProductCode, cake -> cake.getQuantity() != null ? cake.getQuantity() : 0));

        // Delegate logic xử lý file và validation vào ExcelReader
        return excelReader.readAndValidate(file, existingCodes, currentQuantities);
    }

    @Transactional(rollbackFor = Exception.class)
    public CommitResult commit(List<ProductImportDto> validRows) {
        int successCount = 0;
        int failedCount = 0;

        for (ProductImportDto row : validRows) {
            try {
                // Tìm sản phẩm theo productCode
                Optional<Cake> optionalProduct = cakeRepository.findByProductCode(row.getProductCode());
                if (optionalProduct.isPresent()) {
                    // Nếu tồn tại: cập nhật tồn kho (quantity += input)
                    Cake product = optionalProduct.get();
                    int currentQty = product.getQuantity() != null ? product.getQuantity() : 0;
                    product.setQuantity(currentQty + row.getQuantity());
                    cakeRepository.save(product);
                    successCount++;
                } else {
                    // Nếu không tồn tại: bỏ qua (không tạo mới)
                    failedCount++;
                }
            } catch (Exception e) {
                // Ném ngoại lệ để kích hoạt rollback nếu có lỗi database
                throw new RuntimeException("Lỗi khi cập nhật sản phẩm có code: " + row.getProductCode(), e);
            }
        }
        // Trả kết quả
        return new CommitResult(successCount, failedCount);
    }
}
