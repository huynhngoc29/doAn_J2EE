package nhom02.doanmon.util;

import nhom02.doanmon.dto.ErrorRow;
import nhom02.doanmon.dto.ImportResult;
import nhom02.doanmon.dto.ProductImportDto;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;

@Component
public class ExcelReader {

    public ImportResult readAndValidate(MultipartFile file, Set<String> existingCodes, Map<String, Integer> currentQuantities) {
        List<ProductImportDto> validRows = new ArrayList<>();
        List<ErrorRow> errorRows = new ArrayList<>();
        Set<String> processedCodes = new HashSet<>();
        
        int totalRows = 0;

        try (InputStream is = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(is)) {
            
            Sheet sheet = workbook.getSheetAt(0);
            boolean isFirstRow = true;

            for (Row row : sheet) {
                if (isFirstRow) {
                    isFirstRow = false; // Bỏ qua header
                    continue;
                }
                
                // Nếu dòng hoàn toàn rỗng thì có thể kết thúc hoặc bỏ qua
                if (isRowEmpty(row)) {
                    continue;
                }

                totalRows++;
                int rowIndex = row.getRowNum() + 1; // 1-based index (hiển thị cho user)
                
                String productCode = getCellValueAsString(row.getCell(0));
                String quantityStr = getCellValueAsString(row.getCell(1));
                
                List<String> errors = new ArrayList<>();
                Integer quantity = null;
                
                // Validate productCode
                if (productCode == null || productCode.trim().isEmpty()) {
                    errors.add("productCode không được để trống");
                } else {
                    productCode = productCode.trim();
                    if (!existingCodes.contains(productCode)) {
                        errors.add("productCode không tồn tại trong hệ thống");
                    } else if (processedCodes.contains(productCode)) {
                        errors.add("Trùng lặp productCode trong file");
                    }
                }

                // Validate quantity
                if (quantityStr == null || quantityStr.trim().isEmpty()) {
                    errors.add("quantity không được để trống");
                } else {
                    try {
                        // excel values often parse as 10.0 instead of 10
                        double qDouble = Double.parseDouble(quantityStr.trim());
                        quantity = (int) Math.round(qDouble);
                        if (quantity < 0) {
                            errors.add("quantity phải >= 0");
                        }
                    } catch (NumberFormatException e) {
                        errors.add("quantity phải là số nguyên");
                    }
                }

                if (errors.isEmpty()) {
                    processedCodes.add(productCode);
                    ProductImportDto dto = new ProductImportDto(rowIndex, productCode, quantity);
                    int oldQty = currentQuantities.getOrDefault(productCode, 0);
                    dto.setOldQuantity(oldQty);
                    dto.setNewQuantity(oldQty + quantity);
                    validRows.add(dto);
                } else {
                    errorRows.add(new ErrorRow(rowIndex, productCode != null ? productCode : "", String.join(", ", errors)));
                }
            }
            
            ImportResult.Summary summary = new ImportResult.Summary(totalRows, validRows.size(), errorRows.size());
            return new ImportResult(validRows, errorRows, summary);
            
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi đọc file Excel: " + e.getMessage(), e);
        }
    }

    private boolean isRowEmpty(Row row) {
        if (row == null) return true;
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                // If it looks like a number, avoid scientific notation
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
