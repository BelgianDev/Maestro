package be.raftdev.maestro.rest;

import be.raftdev.maestro.exception.ApiError;
import be.raftdev.maestro.exception.ApiException;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {
    private final boolean success;
    private final @NotNull Instant timestamp;
    private final @Nullable Object data;
    private final @Nullable ApiError error;

    private ApiResponse(boolean success, Instant timestamp, @Nullable Object data, @Nullable ApiError error) {
        this.success = success;
        this.timestamp = timestamp;
        this.data = data;
        this.error = error;
    }

    public static ApiResponse success(@Nullable Object data) {
        return new ApiResponse(true, Instant.now(), data, null);
    }

    public static ApiResponse error(@NotNull ApiException exception, @NotNull String path) {
        return error(exception.apiError(path));
    }

    public static ApiResponse error(ApiError error) {
        return new ApiResponse(false, Instant.now(), null, error);
    }

    public boolean isSuccess() {
        return this.success;
    }

    public Instant getTimestamp() {
        return this.timestamp;
    }

    @Nullable
    public Object getData() {
        return this.data;
    }

    @Nullable
    public ApiError getError() {
        return this.error;
    }
}
