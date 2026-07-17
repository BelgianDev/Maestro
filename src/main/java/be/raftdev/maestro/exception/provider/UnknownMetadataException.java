package be.raftdev.maestro.exception.provider;

import be.raftdev.maestro.exception.ApiException;
import org.springframework.http.HttpStatus;

public class UnknownMetadataException extends ApiException {
    private static final int CODE = HttpStatus.NOT_FOUND.value();
    private static final String ERROR = "UNKNOWN_METADATA";

    public UnknownMetadataException(String message) {
        super(message, CODE, ERROR);
    }
}
