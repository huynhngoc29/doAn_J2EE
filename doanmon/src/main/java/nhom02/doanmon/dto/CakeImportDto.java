package nhom02.doanmon.dto;

/**
 * DTO cho việc import bánh từ Excel
 * Hỗ trợ cả TẠO MỚI và CẬP NHẬT
 */
public class CakeImportDto {
    private int rowIndex;
    private String productCode;
    private String name;
    private Double price;
    private String categoryName;
    private Integer quantity;
    private String description;
    
    // Thông tin trạng thái
    private boolean isNew; // true = tạo mới, false = cập nhật
    private Integer oldQuantity; // Số lượng cũ (nếu cập nhật)
    private Integer newQuantity; // Số lượng mới
    
    public CakeImportDto() {
    }
    
    public CakeImportDto(int rowIndex, String productCode, String name, Double price, 
                         String categoryName, Integer quantity, String description) {
        this.rowIndex = rowIndex;
        this.productCode = productCode;
        this.name = name;
        this.price = price;
        this.categoryName = categoryName;
        this.quantity = quantity;
        this.description = description;
    }

    // Getters and Setters
    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    public Integer getOldQuantity() {
        return oldQuantity;
    }

    public void setOldQuantity(Integer oldQuantity) {
        this.oldQuantity = oldQuantity;
    }

    public Integer getNewQuantity() {
        return newQuantity;
    }

    public void setNewQuantity(Integer newQuantity) {
        this.newQuantity = newQuantity;
    }
}

