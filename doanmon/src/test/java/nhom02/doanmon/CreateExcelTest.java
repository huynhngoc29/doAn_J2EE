package nhom02.doanmon;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;

public class CreateExcelTest {

    @Test
    public void generateExcel() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Products");
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("productCode");
        header.createCell(1).setCellValue("quantity");

        // Generate 100 valid products
        for (int i = 1; i <= 100; i++) {
            Row row = sheet.createRow(i);
            // Create a realistic code
            row.createCell(0).setCellValue("PROD-" + String.format("%03d", i));
            
            // Random quantity from 10 to 100
            int quantity = (int) (Math.random() * 90) + 10;
            row.createCell(1).setCellValue(quantity);
        }
        
        // Let's add 5 rows that are purposely faulty for testing invalid validation
        String[] errorCodes = {"ERR-EMPTY", "ERR-MINUS", "ERR-DUP", "ERR-DUP", "PROD-001"};
        int[] errorQtys = {10, -5, 10, 20, -10}; // PROD-001 with minus quantity
        for (int i=0; i<5; i++) {
             Row row = sheet.createRow(100 + i + 1);
             if (!errorCodes[i].equals("ERR-EMPTY")) {
                 row.createCell(0).setCellValue(errorCodes[i]);
             }
             row.createCell(1).setCellValue(errorQtys[i]);
        }

        try (FileOutputStream out = new FileOutputStream("test_products.xlsx")) {
            workbook.write(out);
        }
        workbook.close();
        System.out.println("Excel file generated at: test_products.xlsx");
    }
}
