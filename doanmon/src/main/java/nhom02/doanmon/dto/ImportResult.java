package nhom02.doanmon.dto;

import java.util.List;

public class ImportResult {
    private List<ProductImportDto> validRows;
    private List<ErrorRow> errorRows;
    private Summary summary;

    public ImportResult(List<ProductImportDto> validRows, List<ErrorRow> errorRows, Summary summary) {
        this.validRows = validRows;
        this.errorRows = errorRows;
        this.summary = summary;
    }

    public List<ProductImportDto> getValidRows() {
        return validRows;
    }

    public void setValidRows(List<ProductImportDto> validRows) {
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

        public Summary(int totalRows, int validCount, int errorCount) {
            this.totalRows = totalRows;
            this.validCount = validCount;
            this.errorCount = errorCount;
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
    }
}
