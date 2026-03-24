package nhom02.doanmon.service;

import nhom02.doanmon.dto.CakeImportDto;
import nhom02.doanmon.dto.CakeImportResult;
import nhom02.doanmon.dto.CommitResult;
import nhom02.doanmon.dto.ErrorRow;
import nhom02.doanmon.entity.Cake;
import nhom02.doanmon.entity.Category;
import nhom02.doanmon.repository.CakeRepository;
import nhom02.doanmon.repository.CategoryRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service xử lý Import/Export bánh bằng Excel
 * Hỗ trợ: TẠO MỚI, CẬP NHẬT, XUẤT DỮ LIỆU
 */
@Service
public class CakeImportExportService {

    @Autowired
    private CakeRepository cakeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * XUẤT danh sách bánh ra Excel
     */
    public byte[] exportCakesToExcel() throws Exception {
        List<Cake> cakes = cakeRepository.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Cakes");

        // Tạo header style
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Tạo header row
        Row headerRow = sheet.createRow(0);
        String[] headers = { "productCode", "name", "price", "categoryName", "quantity", "description" };
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Điền dữ liệu
        int rowNum = 1;
        for (Cake cake : cakes) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(cake.getProductCode() != null ? cake.getProductCode() : "");
            row.createCell(1).setCellValue(cake.getName());
            row.createCell(2).setCellValue(cake.getPrice());
            row.createCell(3).setCellValue(cake.getCategory() != null ? cake.getCategory().getName() : "");
            row.createCell(4).setCellValue(cake.getQuantity() != null ? cake.getQuantity() : 0);
            row.createCell(5).setCellValue(cake.getDescription() != null ? cake.getDescription() : "");
        }

        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write to byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }

    /**
     * PREVIEW file Excel trước khi import
     */
    public CakeImportResult previewImport(MultipartFile file) {
        List<CakeImportDto> validRows = new ArrayList<>();
        List<ErrorRow> errorRows = new ArrayList<>();
        Set<String> processedCodes = new HashSet<>();

        // Lấy danh sách productCode và category hiện có
        Map<String, Cake> existingCakes = cakeRepository.findAll().stream()
                .filter(c -> c.getProductCode() != null)
                .collect(Collectors.toMap(Cake::getProductCode, c -> c));

        Map<String, Category> existingCategories = categoryRepository.findAll().stream()
                .collect(Collectors.toMap(Category::getName, c -> c));

        int totalRows = 0;
        int newCount = 0;
        int updateCount = 0;

        try (InputStream is = file.getInputStream();
                Workbook workbook = WorkbookFactory.create(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            boolean isFirstRow = true;

            for (Row row : sheet) {
                if (isFirstRow) {
                    isFirstRow = false;
                    continue;
                }

                if (isRowEmpty(row)) {
                    continue;
                }

                totalRows++;
                int rowIndex = row.getRowNum() + 1;

                // Đọc dữ liệu từ các cột
                String productCode = getCellValueAsString(row.getCell(0));
                String name = getCellValueAsString(row.getCell(1));
                String priceStr = getCellValueAsString(row.getCell(2));
                String categoryName = getCellValueAsString(row.getCell(3));
                String quantityStr = getCellValueAsString(row.getCell(4));
                String description = getCellValueAsString(row.getCell(5));

                List<String> errors = validateRow(productCode, name, priceStr, categoryName,
                        quantityStr, processedCodes, existingCategories);

                if (errors.isEmpty()) {
                    Double price = Double.parseDouble(priceStr.trim());
                    Integer quantity = Integer.parseInt(quantityStr.trim());

                    CakeImportDto dto = new CakeImportDto(rowIndex, productCode.trim(), name.trim(),
                            price, categoryName.trim(), quantity, description);

                    // Kiểm tra tạo mới hay cập nhật
                    if (existingCakes.containsKey(productCode.trim())) {
                        dto.setNew(false);
                        Cake existing = existingCakes.get(productCode.trim());
                        dto.setOldQuantity(existing.getQuantity());
                        dto.setNewQuantity(existing.getQuantity() + quantity);
                        updateCount++;
                    } else {
                        dto.setNew(true);
                        dto.setOldQuantity(0);
                        dto.setNewQuantity(quantity);
                        newCount++;
                    }

                    validRows.add(dto);
                    processedCodes.add(productCode.trim());
                } else {
                    errorRows.add(new ErrorRow(rowIndex, productCode != null ? productCode : "",
                            String.join(", ", errors)));
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi đọc file Excel: " + e.getMessage(), e);
        }

        CakeImportResult.Summary summary = new CakeImportResult.Summary(
                totalRows, validRows.size(), errorRows.size(), newCount, updateCount);
        return new CakeImportResult(validRows, errorRows, summary);
    }

    /**
     * COMMIT - Lưu dữ liệu vào database
     */
    @Transactional(rollbackFor = Exception.class)
    public CommitResult commitImport(List<CakeImportDto> validRows) {
        int successCount = 0;
        int failedCount = 0;
        int newCount = 0;
        int updateCount = 0;

        for (CakeImportDto dto : validRows) {
            try {
                Optional<Cake> optionalCake = cakeRepository.findByProductCode(dto.getProductCode());

                if (optionalCake.isPresent()) {
                    // CẬP NHẬT bánh hiện có
                    Cake cake = optionalCake.get();
                    cake.setName(dto.getName());
                    cake.setPrice(dto.getPrice());
                    cake.setQuantity(cake.getQuantity() + dto.getQuantity());
                    if (dto.getDescription() != null && !dto.getDescription().trim().isEmpty()) {
                        cake.setDescription(dto.getDescription());
                    }

                    // Cập nhật category nếu có
                    Category category = categoryRepository.findByName(dto.getCategoryName()).orElse(null);
                    if (category != null) {
                        cake.setCategory(category);
                    }

                    cakeRepository.save(cake);
                    updateCount++;
                    successCount++;
                } else {
                    // TẠO MỚI bánh
                    Cake newCake = new Cake();
                    newCake.setProductCode(dto.getProductCode());
                    newCake.setName(dto.getName());
                    newCake.setPrice(dto.getPrice());
                    newCake.setQuantity(dto.getQuantity());
                    newCake.setDescription(dto.getDescription());

                    // Tìm category
                    Category category = categoryRepository.findByName(dto.getCategoryName()).orElse(null);
                    if (category != null) {
                        newCake.setCategory(category);
                    }

                    cakeRepository.save(newCake);
                    newCount++;
                    successCount++;
                }
            } catch (Exception e) {
                failedCount++;
                throw new RuntimeException("Lỗi khi xử lý productCode: " + dto.getProductCode(), e);
            }
        }

        return new CommitResult(successCount, failedCount);
    }

    /**
     * Validate một dòng dữ liệu
     */
    private List<String> validateRow(String productCode, String name, String priceStr,
            String categoryName, String quantityStr,
            Set<String> processedCodes,
            Map<String, Category> existingCategories) {
        List<String> errors = new ArrayList<>();

        // 1. Validate productCode
        if (productCode == null || productCode.trim().isEmpty()) {
            errors.add("productCode không được để trống");
        } else if (processedCodes.contains(productCode.trim())) {
            errors.add("Trùng lặp productCode trong file");
        } else if (productCode.trim().length() > 100) {
            errors.add("productCode không được quá 100 ký tự");
        }

        // 2. Validate name
        if (name == null || name.trim().isEmpty()) {
            errors.add("name không được để trống");
        } else if (name.trim().length() > 100) {
            errors.add("name không được quá 100 ký tự");
        }

        // 3. Validate price
        if (priceStr == null || priceStr.trim().isEmpty()) {
            errors.add("price không được để trống");
        } else {
            try {
                double price = Double.parseDouble(priceStr.trim());
                if (price <= 0) {
                    errors.add("price phải > 0");
                }
                if (price > 1000000) {
                    errors.add("price không hợp lý (quá lớn)");
                }
            } catch (NumberFormatException e) {
                errors.add("price phải là số");
            }
        }

        // 4. Validate categoryName
        if (categoryName == null || categoryName.trim().isEmpty()) {
            errors.add("categoryName không được để trống");
        } else if (!existingCategories.containsKey(categoryName.trim())) {
            errors.add("categoryName không tồn tại trong hệ thống");
        }

        // 5. Validate quantity
        if (quantityStr == null || quantityStr.trim().isEmpty()) {
            errors.add("quantity không được để trống");
        } else {
            try {
                int quantity = Integer.parseInt(quantityStr.trim());
                if (quantity < 0) {
                    errors.add("quantity phải >= 0");
                }
                if (quantity > 100000) {
                    errors.add("quantity không hợp lý (quá lớn)");
                }
            } catch (NumberFormatException e) {
                errors.add("quantity phải là số nguyên");
            }
        }

        return errors;
    }

    /**
     * Kiểm tra dòng có rỗng không
     */
    private boolean isRowEmpty(Row row) {
        if (row == null)
            return true;
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    /**
     * Lấy giá trị cell dưới dạng String
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null)
            return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                double d = cell.getNumericCellValue();
                if (d == (long) d) {
                    return String.format("%d", (long) d);
                } else {
                    return String.format("%s", d);
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return String.valueOf(cell.getNumericCellValue());
                } catch (Exception e) {
                    return cell.getStringCellValue();
                }
            default:
                return "";
        }
    }
}
