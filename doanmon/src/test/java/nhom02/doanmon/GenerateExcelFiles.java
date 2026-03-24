package nhom02.doanmon;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Class để tạo các file Excel mẫu (.xlsx)
 * Chạy main() để generate tất cả các file
 */
public class GenerateExcelFiles {

    public static void main(String[] args) {
        try {
            System.out.println("Bắt đầu tạo các file Excel...");

            createFile1_MauImportBanh();
            createFile2_CapNhatSoLuong();
            createFile3_TaoMoiVaCapNhat();
            createFileLoi1_TruongTrong();
            createFileLoi2_GiaTriKhongHopLe();
            createFileLoi3_TrungLapVaCategory();
            createFileLoi4_QuaDai();
            createFileLoi5_TatCaLoi();
            createFileLoi6_MotPhanDungMotPhanSai();

            System.out.println("✅ Đã tạo xong tất cả các file Excel!");
            System.out.println("📁 Vị trí: doanmon/");
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi tạo file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createFile1_MauImportBanh() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Cakes");

        // Header
        Row headerRow = sheet.createRow(0);
        createHeaderRow(headerRow);

        // Data
        Object[][] data = {
                { "CAKE-CHOCO-001", "Chocolate Fudge Cake", 28.99, "Chocolate", 50,
                        "Rich dark chocolate cake with fudge frosting" },
                { "CAKE-CHOCO-002", "Chocolate Truffle Delight", 32.50, "Chocolate", 30,
                        "Premium chocolate truffle cake" },
                { "CAKE-CHOCO-003", "Double Chocolate Dream", 26.75, "Chocolate", 40, "Double layer chocolate sponge" },
                { "CAKE-VANILLA-001", "Classic Vanilla Sponge", 18.99, "Vanilla", 60, "Light and fluffy vanilla cake" },
                { "CAKE-VANILLA-002", "Vanilla Bean Supreme", 22.50, "Vanilla", 35, "Made with real vanilla beans" },
                { "CAKE-FRUIT-001", "Strawberry Shortcake", 24.99, "Fruit", 45, "Fresh strawberries with cream" },
                { "CAKE-FRUIT-002", "Blueberry Bliss", 23.50, "Fruit", 40, "Loaded with fresh blueberries" },
                { "CAKE-FRUIT-003", "Mixed Berry Delight", 25.99, "Fruit", 30,
                        "Strawberry raspberry and blueberry mix" },
                { "CAKE-SPECIAL-001", "Red Velvet Classic", 29.99, "Chocolate", 25,
                        "Traditional red velvet with cream cheese" },
                { "CAKE-SPECIAL-002", "Carrot Cake Supreme", 27.50, "Vanilla", 20, "Moist carrot cake with walnuts" }
        };

        fillData(sheet, data);
        autoSizeColumns(sheet, 6);

        writeToFile(workbook, "FILE_1_MAU_IMPORT_BANH.xlsx");
    }

    private static void createFile2_CapNhatSoLuong() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Cakes");

        Row headerRow = sheet.createRow(0);
        createHeaderRow(headerRow);

        Object[][] data = {
                { "CAKE-CHOCO-001", "Chocolate Fudge Cake", 28.99, "Chocolate", 20, "Restocking chocolate fudge" },
                { "CAKE-CHOCO-002", "Chocolate Truffle Delight", 32.50, "Chocolate", 15, "Restocking truffle cake" },
                { "CAKE-VANILLA-001", "Classic Vanilla Sponge", 18.99, "Vanilla", 30, "Restocking vanilla sponge" },
                { "CAKE-FRUIT-001", "Strawberry Shortcake", 24.99, "Fruit", 25, "Restocking strawberry cake" },
                { "CAKE-SPECIAL-001", "Red Velvet Classic", 29.99, "Chocolate", 10, "Restocking red velvet" }
        };

        fillData(sheet, data);
        autoSizeColumns(sheet, 6);

        writeToFile(workbook, "FILE_2_CAP_NHAT_SO_LUONG.xlsx");
    }

    private static void createFile3_TaoMoiVaCapNhat() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Cakes");

        Row headerRow = sheet.createRow(0);
        createHeaderRow(headerRow);

        Object[][] data = {
                { "CAKE-CHOCO-001", "Chocolate Fudge Cake Updated", 30.99, "Chocolate", 10,
                        "Updated price and restocking" },
                { "CAKE-NEW-TIRAMISU-001", "Tiramisu Classic", 34.99, "Chocolate", 25,
                        "Italian coffee-flavored dessert" },
                { "CAKE-NEW-TIRAMISU-002", "Tiramisu Deluxe", 38.50, "Chocolate", 15,
                        "Premium tiramisu with mascarpone" },
                { "CAKE-NEW-CHEESE-001", "New York Cheesecake", 32.99, "Vanilla", 30, "Classic creamy cheesecake" },
                { "CAKE-NEW-CHEESE-002", "Blueberry Cheesecake", 35.50, "Fruit", 20,
                        "Cheesecake with blueberry topping" },
                { "CAKE-VANILLA-001", "Classic Vanilla Sponge", 19.99, "Vanilla", 15, "Price updated and restocking" },
                { "CAKE-NEW-MATCHA-001", "Matcha Green Tea Cake", 29.99, "Vanilla", 25, "Japanese matcha flavor" },
                { "CAKE-NEW-LEMON-001", "Lemon Drizzle Cake", 21.50, "Fruit", 35, "Tangy lemon cake with glaze" },
                { "CAKE-NEW-COCONUT-001", "Coconut Paradise", 26.99, "Vanilla", 20, "Tropical coconut cake" },
                { "CAKE-SPECIAL-001", "Red Velvet Classic", 31.99, "Chocolate", 5, "Price increase and small restock" }
        };

        fillData(sheet, data);
        autoSizeColumns(sheet, 6);

        writeToFile(workbook, "FILE_3_TAO_MOI_VA_CAP_NHAT.xlsx");
    }

    private static void createFileLoi1_TruongTrong() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Cakes");

        Row headerRow = sheet.createRow(0);
        createHeaderRow(headerRow);

        Object[][] data = {
                { "", "Chocolate Cake Missing Code", 25.99, "Chocolate", 10, "productCode is empty" },
                { "CAKE-001", "", 18.50, "Vanilla", 5, "name is empty" },
                { "CAKE-002", "Strawberry Cake", null, "Fruit", 20, "price is empty" },
                { "CAKE-003", "Vanilla Dream", 29.99, "", 15, "categoryName is empty" },
                { "CAKE-004", "Lemon Cake", 22.50, "Chocolate", null, "quantity is empty" }
        };

        fillData(sheet, data);
        autoSizeColumns(sheet, 6);

        writeToFile(workbook, "FILE_LOI_1_TRUONG_TRONG.xlsx");
    }

    private static void createFileLoi2_GiaTriKhongHopLe() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Cakes");

        Row headerRow = sheet.createRow(0);
        createHeaderRow(headerRow);

        Object[][] data = {
                { "CAKE-001", "Price is Zero", 0.0, "Chocolate", 10, "price must be > 0" },
                { "CAKE-002", "Negative Price", -10.50, "Vanilla", 5, "price is negative" },
                { "CAKE-003", "Price Too High", 2000000.0, "Fruit", 20, "price exceeds 1000000" },
                { "CAKE-004", "Quantity is Decimal", 25.99, "Chocolate", 10.5, "quantity must be integer" },
                { "CAKE-005", "Negative Quantity", 18.50, "Vanilla", -5, "quantity is negative" },
                { "CAKE-006", "Quantity Too High", 22.50, "Fruit", 200000, "quantity exceeds 100000" },
                { "CAKE-007", "Price is Text", "abc", "Chocolate", 10, "price is not a number" },
                { "CAKE-008", "Quantity is Text", 25.99, "Vanilla", "ten", "quantity is not a number" }
        };

        fillData(sheet, data);
        autoSizeColumns(sheet, 6);

        writeToFile(workbook, "FILE_LOI_2_GIA_TRI_KHONG_HOP_LE.xlsx");
    }

    private static void createFileLoi3_TrungLapVaCategory() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Cakes");

        Row headerRow = sheet.createRow(0);
        createHeaderRow(headerRow);

        Object[][] data = {
                { "CAKE-001", "First Chocolate Cake", 25.99, "Chocolate", 10, "This is fine" },
                { "CAKE-002", "Vanilla Cake", 18.50, "Vanilla", 5, "This is fine" },
                { "CAKE-001", "Duplicate Chocolate Cake", 29.99, "Chocolate", 15, "Duplicate productCode CAKE-001" },
                { "CAKE-003", "Unknown Category Cake", 22.50, "UnknownCategory", 20, "Category does not exist" },
                { "CAKE-004", "Another Unknown", 19.99, "Dessert", 25, "Category Dessert does not exist" },
                { "CAKE-005", "Ice Cream Cake", 24.50, "IceCream", 30, "Category IceCream does not exist" }
        };

        fillData(sheet, data);
        autoSizeColumns(sheet, 6);

        writeToFile(workbook, "FILE_LOI_3_TRUNG_LAP_VA_CATEGORY.xlsx");
    }

    private static void createFileLoi4_QuaDai() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Cakes");

        Row headerRow = sheet.createRow(0);
        createHeaderRow(headerRow);

        Object[][] data = {
                { "CAKE-VERY-LONG-PRODUCT-CODE-THAT-EXCEEDS-ONE-HUNDRED-CHARACTERS-LIMIT-AND-WILL-CAUSE-VALIDATION-ERROR-FOR-SURE",
                        "Short Name", 25.99, "Chocolate", 10, "productCode exceeds 100 characters" },
                { "CAKE-001",
                        "This is a very long cake name that definitely exceeds one hundred characters limit and will cause validation error",
                        18.50, "Vanilla", 5, "name exceeds 100 characters" },
                { "CAKE-002", "Normal Name", 22.50, "Chocolate", 20, "This one is fine" }
        };

        fillData(sheet, data);
        autoSizeColumns(sheet, 6);

        writeToFile(workbook, "FILE_LOI_4_QUA_DAI.xlsx");
    }

    private static void createFileLoi5_TatCaLoi() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Cakes");

        Row headerRow = sheet.createRow(0);
        createHeaderRow(headerRow);

        Object[][] data = {
                { "", "Empty ProductCode", 25.99, "Chocolate", 10, "ERROR: productCode is empty" },
                { "CAKE-001", "", 18.50, "Vanilla", 5, "ERROR: name is empty" },
                { "CAKE-002", "Negative Price", -10.0, "Fruit", 20, "ERROR: price is negative" },
                { "CAKE-003", "No Category", 29.99, "", 15, "ERROR: categoryName is empty" },
                { "CAKE-004", "Bad Quantity", 22.50, "Chocolate", "abc", "ERROR: quantity is not a number" },
                { "CAKE-001", "Duplicate Code", 19.99, "Chocolate", 10, "ERROR: duplicate productCode" },
                { "CAKE-005", "Unknown Category", 24.99, "UnknownCat", 25, "ERROR: category does not exist" },
                { "CAKE-VERY-LONG-CODE-EXCEEDS-LIMIT-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
                        "Too Long", 30.0, "Chocolate", 5, "ERROR: productCode too long" },
                { "CAKE-006", "Price Zero", 0.0, "Vanilla", 10, "ERROR: price must be > 0" },
                { "CAKE-007", "Negative Qty", 25.99, "Fruit", -5, "ERROR: quantity is negative" }
        };

        fillData(sheet, data);
        autoSizeColumns(sheet, 6);

        writeToFile(workbook, "FILE_LOI_5_TAT_CA_LOI.xlsx");
    }

    private static void createFileLoi6_MotPhanDungMotPhanSai() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Cakes");

        Row headerRow = sheet.createRow(0);
        createHeaderRow(headerRow);

        Object[][] data = {
                { "CAKE-GOOD-001", "Perfect Chocolate Cake", 25.99, "Chocolate", 50, "This row is completely valid" },
                { "CAKE-BAD-001", "Missing Price", null, "Vanilla", 30, "ERROR: price is empty" },
                { "CAKE-GOOD-002", "Strawberry Delight", 22.50, "Fruit", 40, "This row is valid" },
                { "CAKE-BAD-002", "Unknown Category", 18.99, "Bakery", 25, "ERROR: category Bakery does not exist" },
                { "CAKE-GOOD-003", "Vanilla Supreme", 19.99, "Vanilla", 35, "This row is valid" },
                { "CAKE-BAD-003", "Negative Quantity", 24.50, "Chocolate", -10, "ERROR: quantity is negative" },
                { "CAKE-GOOD-004", "Blueberry Cake", 21.99, "Fruit", 45, "This row is valid" },
                { "CAKE-BAD-004", "", 29.99, "Chocolate", 20, "ERROR: name is empty" },
                { "CAKE-GOOD-005", "Lemon Tart", 17.50, "Fruit", 30, "This row is valid" },
                { "CAKE-BAD-005", "Price Text", "abc", "Vanilla", 15, "ERROR: price is not a number" }
        };

        fillData(sheet, data);
        autoSizeColumns(sheet, 6);

        writeToFile(workbook, "FILE_LOI_6_MOT_PHAN_DUNG_MOT_PHAN_SAI.xlsx");
    }

    // Helper methods
    private static void createHeaderRow(Row headerRow) {
        String[] headers = { "productCode", "name", "price", "categoryName", "quantity", "description" };
        CellStyle headerStyle = headerRow.getSheet().getWorkbook().createCellStyle();
        Font headerFont = headerRow.getSheet().getWorkbook().createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    private static void fillData(Sheet sheet, Object[][] data) {
        int rowNum = 1;
        for (Object[] rowData : data) {
            Row row = sheet.createRow(rowNum++);
            for (int i = 0; i < rowData.length; i++) {
                Cell cell = row.createCell(i);
                Object value = rowData[i];

                if (value == null) {
                    cell.setCellValue("");
                } else if (value instanceof String) {
                    cell.setCellValue((String) value);
                } else if (value instanceof Double) {
                    cell.setCellValue((Double) value);
                } else if (value instanceof Integer) {
                    cell.setCellValue((Integer) value);
                }
            }
        }
    }

    private static void autoSizeColumns(Sheet sheet, int columnCount) {
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private static void writeToFile(Workbook workbook, String filename) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(filename)) {
            workbook.write(fileOut);
            System.out.println("✅ Đã tạo: " + filename);
        }
        workbook.close();
    }
}
