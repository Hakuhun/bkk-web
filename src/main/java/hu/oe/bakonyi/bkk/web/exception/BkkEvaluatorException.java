package hu.oe.bakonyi.bkk.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BkkEvaluatorException extends ResponseStatusException {
    private static final HttpStatus SERVICE_UNAVAILABLE = HttpStatus.SERVICE_UNAVAILABLE;

    private BkkEvaluatorException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public BkkEvaluatorException(String message) {
        super(SERVICE_UNAVAILABLE, message);
    }
}
