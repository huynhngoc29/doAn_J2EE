package nhom02.doanmon.dto;

import java.util.List;

/**
 * Kết quả import bánh từ Excel
 */
public class CakeImportResult {
    private List<CakeImportDto> validRows;
    private List<ErrorRow> errorRows;
    private Summary summary;

    public CakeImportResult(List<CakeImportDto> validRows, List<ErrorRow> errorRows, Summary summary) {
        this.validRows = validRows;
        this.errorRows = errorRows;
        this.summary = summary;
    }

    public List<CakeImportDto> getValidRows() {
        return validRows;
    }

    public void setValidRows(List<CakeImportDto> validRows) {
        this.validRows = validRows;
    }

    public List<ErrorRow> getErrorRows() {
        return errorRows;
    }

    public void setErrorRows(List<ErrorRow> errorRows) {
        this.errorRows = errorRows;
    }

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    public static class Summary {
        private int totalRows;
        private int validCount;
        private int errorCount;
        private int newCount;      // Số bánh mới
        private int updateCount;   // Số bánh cập nhật

        public Summary(int totalRows, int validCount, int errorCount, int newCount, int updateCount) {
            this.totalRows = totalRows;
            this.validCount = validCount;
            this.errorCount = errorCount;
            this.newCount = newCount;
            this.updateCount = updateCount;
        }

        public int getTotalRows() {
            return totalRows;
        }

        public void setTotalRows(int totalRows) {
            this.totalRows = totalRows;
        }

        public int getValidCount() {
            return validCount;
        }

        public void setValidCount(int validCount) {
            this.validCount = validCount;
        }

        public int getErrorCount() {
            return errorCount;
        }

        public void setErrorCount(int errorCount) {
            this.errorCount = errorCount;
        }

        public int getNewCount() {
            return newCount;
        }

        public void setNewCount(int newCount) {
            this.newCount = newCount;
        }

        public int getUpdateCount() {
            return updateCount;
        }

        public void setUpdateCount(int updateCount) {
            this.updateCount = updateCount;
        }
    }
}

