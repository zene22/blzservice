package info.furbach.blz.rest.blz.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class BLZServiceException extends Exception {

    @Getter
    private Map<String, Object> fields = new HashMap<>();

    public BLZServiceException(String message) {
        super(message);
    }

    public BLZServiceException(String message, Map<String, Object> fields) {
        super(message);
        this.fields = fields;
    }

    public BLZServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
