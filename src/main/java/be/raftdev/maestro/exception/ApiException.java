package be.raftdev.maestro.exception;

public class ApiException extends RuntimeException {
    private final int status;
    private final String error;

    public ApiException(String message, int status, String error) {
        super(message);
        this.status = status;
        this.error = error;
    }

    public ApiException(String message, Throwable cause, int status, String error) {
        super(message, cause);
        this.status = status;
        this.error = error;
    }

    public ApiException(Throwable cause, int status, String error) {
        super(cause);
        this.status = status;
        this.error = error;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public ApiError apiError(String path) {
        return new ApiError(this.error, this.getMessage(), path);
    }
}