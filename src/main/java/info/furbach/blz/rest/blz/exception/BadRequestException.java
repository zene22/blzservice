package info.furbach.blz.rest.blz.exception;

public class BadRequestException extends BLZServiceException {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
