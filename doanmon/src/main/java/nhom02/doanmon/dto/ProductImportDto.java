package nhom02.doanmon.dto;

public class ProductImportDto {
    private int rowIndex;
    private String productCode;
    private Integer quantity;
    
    // Giả lập thêm
    private Integer oldQuantity;
    private Integer newQuantity;

    public ProductImportDto() {
    }

    public ProductImportDto(int rowIndex, String productCode, Integer quantity) {
        this.rowIndex = rowIndex;
        this.productCode = productCode;
        this.quantity = quantity;
    }

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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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
