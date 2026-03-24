package nhom02.doanmon.dto;

public class ErrorRow {
    private int rowIndex;
    private String productCode;
    private String errorMessage;

    public ErrorRow(int rowIndex, String productCode, String errorMessage) {
        this.rowIndex = rowIndex;
        this.productCode = productCode;
        this.errorMessage = errorMessage;
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

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
