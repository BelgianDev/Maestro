package be.raftdev.maestro.exception.provider;

import be.raftdev.maestro.exception.ApiException;
import org.springframework.http.HttpStatus;

public class NoSuchProviderException extends ApiException {
    private static final int CODE = HttpStatus.BAD_REQUEST.value();
    private static final String ERROR = "NO_SUCH_PROVIDER";

    public NoSuchProviderException() {
        super("No such provider was found.", CODE, ERROR);
    }

    public NoSuchProviderException(String message) {
        super(message, CODE, ERROR);
    }
}
