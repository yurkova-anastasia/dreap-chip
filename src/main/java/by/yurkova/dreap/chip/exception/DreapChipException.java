package by.yurkova.dreap.chip.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class DreapChipException extends RuntimeException {
    private final HttpStatus httpStatus;

    public DreapChipException(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public DreapChipException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
