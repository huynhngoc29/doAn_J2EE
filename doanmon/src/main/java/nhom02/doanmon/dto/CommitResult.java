package nhom02.doanmon.dto;

public class CommitResult {
    private int successCount;
    private int failedCount;

    public CommitResult() {
    }

    public CommitResult(int successCount, int failedCount) {
        this.successCount = successCount;
        this.failedCount = failedCount;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(int failedCount) {
        this.failedCount = failedCount;
    }
}
