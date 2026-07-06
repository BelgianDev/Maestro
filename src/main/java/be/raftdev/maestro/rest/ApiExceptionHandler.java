package be.raftdev.maestro.rest;

import be.raftdev.maestro.exception.ApiError;
import be.raftdev.maestro.exception.ApiException;
import be.raftdev.maestro.util.LogUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class ApiExceptionHandler {
    private static final Logger LOGGER = LogUtils.getLogger();

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse> handle(ApiException ex, HttpServletRequest request) {
        return ResponseEntity
                .status(ex.getStatus())
                .body(ApiResponse.error(
                        new ApiError(
                                ex.getError(),
                                ex.getMessage(),
                                request.getRequestURI()
                        )
                ));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse> handleMissingEndpoint(NoResourceFoundException ex,
                                                             HttpServletRequest req) {

        return ResponseEntity.status(404).body(
                ApiResponse.error(
                        new ApiError(
                                "ENDPOINT_NOT_FOUND",
                                "Route does not exist",
                                req.getRequestURI()
                        )
                )
        );
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse> handleMissingParameter(MissingServletRequestParameterException ex, HttpServletRequest request) {
        return ResponseEntity.status(400).body(
                ApiResponse.error(
                        new ApiError(
                                "MISSING_PARAMS",
                                "Missing required parameters",
                                request.getRequestURI()
                        )
                )
        );
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ResponseEntity<ApiResponse> handleMissingParameter(InvalidDataAccessApiUsageException ex, HttpServletRequest request) {
        return ResponseEntity.status(400).body(
                ApiResponse.error(
                        new ApiError(
                                "MISSING_BODY_PARAMS",
                                "Missing required body parameters",
                                request.getRequestURI()
                        )
                )
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse> handleMissingParameter(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        return ResponseEntity.status(400).body(
                ApiResponse.error(
                        new ApiError(
                                "INVALID_REQUEST",
                                "Invalid request, endpoint is valid, but the request is invalid",
                                request.getRequestURI()
                        )
                )
        );
    }


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse> handleMissingParameter(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        return ResponseEntity.status(400).body(
                ApiResponse.error(
                        new ApiError(
                                "INVALID_METHOD",
                                ex.getMessage(),
                                request.getRequestURI()
                        )

                )
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleAll(Exception ex, HttpServletRequest request) {
        LOGGER.error("Unexpected error", ex);

        return ResponseEntity.status(500).body(
                ApiResponse.error(
                        new ApiError(
                                "INTERNAL_SERVER_ERROR",
                                "Unexpected error",
                                request.getRequestURI()
                        )
                )
        );
    }
}