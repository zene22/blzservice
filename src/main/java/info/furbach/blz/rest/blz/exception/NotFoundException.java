package info.furbach.blz.rest.blz.exception;

import lombok.Builder;
import lombok.Singular;

import java.util.Map;

public class NotFoundException extends BLZServiceException {

    @Builder
    public NotFoundException(String message, @Singular("field") Map<String, Object> fields) {
        super(message, fields);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(String message) {
        super(message);
    }
}
