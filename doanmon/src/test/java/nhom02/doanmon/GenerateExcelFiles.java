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

        // Data - Updated with new categories
        Object[][] data = {
                { "CAKE-BDAY-001", "Rainbow Birthday Cake", 35.99, "Birthday Cakes", 50,
                        "Colorful rainbow layers perfect for birthdays" },
                { "CAKE-BDAY-002", "Unicorn Dream Cake", 42.50, "Birthday Cakes", 30,
                        "Magical unicorn themed birthday cake" },
                { "CAKE-BDAY-003", "Chocolate Birthday Delight", 38.75, "Birthday Cakes", 40,
                        "Rich chocolate birthday cake with sprinkles" },
                { "CAKE-WEDDING-001", "Classic White Wedding Cake", 150.00, "Wedding Cakes", 15,
                        "Elegant 3-tier white wedding cake" },
                { "CAKE-WEDDING-002", "Rose Garden Wedding Cake", 180.00, "Wedding Cakes", 10,
                        "Beautiful rose decorated wedding cake" },
                { "CAKE-CHEESE-001", "New York Cheesecake", 28.99, "Cheese Cakes", 45,
                        "Classic creamy New York style cheesecake" },
                { "CAKE-CHEESE-002", "Blueberry Cheesecake", 32.50, "Cheese Cakes", 40,
                        "Cheesecake with fresh blueberry topping" },
                { "CAKE-CHEESE-003", "Strawberry Cheesecake", 32.50, "Cheese Cakes", 35,
                        "Cheesecake with strawberry compote" },
                { "CAKE-GATAUX-001", "Black Forest Gataux", 45.99, "Gataux", 25,
                        "Traditional Black Forest gataux with cherries" },
                { "CAKE-GATAUX-002", "Opera Gataux", 48.50, "Gataux", 20,
                        "French opera gataux with coffee and chocolate" }
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
                { "CAKE-BDAY-001", "Rainbow Birthday Cake", 35.99, "Birthday Cakes", 20, "Restocking birthday cakes" },
                { "CAKE-BDAY-002", "Unicorn Dream Cake", 42.50, "Birthday Cakes", 15, "Restocking unicorn cakes" },
                { "CAKE-WEDDING-001", "Classic White Wedding Cake", 150.00, "Wedding Cakes", 5,
                        "Restocking wedding cakes" },
                { "CAKE-CHEESE-001", "New York Cheesecake", 28.99, "Cheese Cakes", 25, "Restocking cheesecakes" },
                { "CAKE-GATAUX-001", "Black Forest Gataux", 45.99, "Gataux", 10, "Restocking gataux" }
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
                { "CAKE-BDAY-001", "Rainbow Birthday Cake Updated", 38.99, "Birthday Cakes", 10,
                        "Updated price and restocking" },
                { "CAKE-NEW-PRINCESS-001", "Princess Castle Cake", 55.00, "Birthday Cakes", 25,
                        "Beautiful princess themed castle cake" },
                { "CAKE-NEW-SUPERHERO-001", "Superhero Birthday Cake", 48.50, "Birthday Cakes", 15,
                        "Action-packed superhero themed cake" },
                { "CAKE-NEW-WEDDING-003", "Floral Wedding Cake", 200.00, "Wedding Cakes", 8,
                        "Elegant floral decorated wedding cake" },
                { "CAKE-CHEESE-002", "Blueberry Cheesecake Updated", 35.50, "Cheese Cakes", 20,
                        "Updated cheesecake with blueberry topping" },
                { "CAKE-NEW-CHEESE-004", "Mango Cheesecake", 34.99, "Cheese Cakes", 15,
                        "Tropical mango cheesecake" },
                { "CAKE-NEW-GATAUX-003", "Strawberry Gataux", 42.99, "Gataux", 25, "Fresh strawberry gataux" },
                { "CAKE-NEW-GATAUX-004", "Chocolate Mousse Gataux", 46.50, "Gataux", 18,
                        "Rich chocolate mousse gataux" },
                { "CAKE-GATAUX-001", "Black Forest Gataux Updated", 48.99, "Gataux", 5,
                        "Price increase and small restock" },
                { "CAKE-NEW-BDAY-004", "Dinosaur Birthday Cake", 44.99, "Birthday Cakes", 20,
                        "Fun dinosaur themed birthday cake" }
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
                { "CAKE-001", "Price is Zero", 0.0, "Birthday Cakes", 10, "price must be > 0" },
                { "CAKE-002", "Negative Price", -10.50, "Wedding Cakes", 5, "price is negative" },
                { "CAKE-003", "Price Too High", 2000000.0, "Cheese Cakes", 20, "price exceeds 1000000" },
                { "CAKE-004", "Quantity is Decimal", 25.99, "Gataux", 10.5, "quantity must be integer" },
                { "CAKE-005", "Negative Quantity", 18.50, "Birthday Cakes", -5, "quantity is negative" },
                { "CAKE-006", "Quantity Too High", 22.50, "Wedding Cakes", 200000, "quantity exceeds 100000" },
                { "CAKE-007", "Price is Text", "abc", "Cheese Cakes", 10, "price is not a number" },
                { "CAKE-008", "Quantity is Text", 25.99, "Gataux", "ten", "quantity is not a number" }
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
                { "CAKE-001", "First Birthday Cake", 35.99, "Birthday Cakes", 10, "This is fine" },
                { "CAKE-002", "Wedding Cake", 150.00, "Wedding Cakes", 5, "This is fine" },
                { "CAKE-001", "Duplicate Birthday Cake", 38.99, "Birthday Cakes", 15,
                        "Duplicate productCode CAKE-001" },
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
                        "Short Name", 25.99, "Birthday Cakes", 10, "productCode exceeds 100 characters" },
                { "CAKE-001",
                        "This is a very long cake name that definitely exceeds one hundred characters limit and will cause validation error",
                        18.50, "Wedding Cakes", 5, "name exceeds 100 characters" },
                { "CAKE-002", "Normal Name", 22.50, "Cheese Cakes", 20, "This one is fine" }
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
                { "", "Empty ProductCode", 25.99, "Birthday Cakes", 10, "ERROR: productCode is empty" },
                { "CAKE-001", "", 18.50, "Wedding Cakes", 5, "ERROR: name is empty" },
                { "CAKE-002", "Negative Price", -10.0, "Cheese Cakes", 20, "ERROR: price is negative" },
                { "CAKE-003", "No Category", 29.99, "", 15, "ERROR: categoryName is empty" },
                { "CAKE-004", "Bad Quantity", 22.50, "Gataux", "abc", "ERROR: quantity is not a number" },
                { "CAKE-001", "Duplicate Code", 19.99, "Birthday Cakes", 10, "ERROR: duplicate productCode" },
                { "CAKE-005", "Unknown Category", 24.99, "UnknownCat", 25, "ERROR: category does not exist" },
                { "CAKE-VERY-LONG-CODE-EXCEEDS-LIMIT-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
                        "Too Long", 30.0, "Wedding Cakes", 5, "ERROR: productCode too long" },
                { "CAKE-006", "Price Zero", 0.0, "Cheese Cakes", 10, "ERROR: price must be > 0" },
                { "CAKE-007", "Negative Qty", 25.99, "Gataux", -5, "ERROR: quantity is negative" }
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
                { "CAKE-GOOD-001", "Perfect Birthday Cake", 35.99, "Birthday Cakes", 50,
                        "This row is completely valid" },
                { "CAKE-BAD-001", "Missing Price", null, "Wedding Cakes", 30, "ERROR: price is empty" },
                { "CAKE-GOOD-002", "Cheesecake Delight", 28.50, "Cheese Cakes", 40, "This row is valid" },
                { "CAKE-BAD-002", "Unknown Category", 18.99, "Bakery", 25, "ERROR: category Bakery does not exist" },
                { "CAKE-GOOD-003", "Gataux Supreme", 45.99, "Gataux", 35, "This row is valid" },
                { "CAKE-BAD-003", "Negative Quantity", 24.50, "Birthday Cakes", -10, "ERROR: quantity is negative" },
                { "CAKE-GOOD-004", "Wedding Cake Special", 175.00, "Wedding Cakes", 8, "This row is valid" },
                { "CAKE-BAD-004", "", 29.99, "Cheese Cakes", 20, "ERROR: name is empty" },
                { "CAKE-GOOD-005", "Opera Gataux", 48.50, "Gataux", 30, "This row is valid" },
                { "CAKE-BAD-005", "Price Text", "abc", "Birthday Cakes", 15, "ERROR: price is not a number" }
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
