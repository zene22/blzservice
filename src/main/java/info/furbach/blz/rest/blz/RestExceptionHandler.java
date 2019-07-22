package info.furbach.blz.rest.blz;

import info.furbach.blz.rest.blz.exception.BLZServiceException;
import info.furbach.blz.rest.blz.exception.BadRequestException;
import info.furbach.blz.rest.blz.exception.Message;
import info.furbach.blz.rest.blz.exception.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({BLZServiceException.class})
    public ResponseEntity<Message> handleDmsServiceException(Exception e, WebRequest request) {
        if (e instanceof NotFoundException) {
            return ResponseEntity.status(NOT_FOUND).body(generateMessageBody(e));
        }
        if (e instanceof BadRequestException) {
            return ResponseEntity.status(BAD_REQUEST).body(generateMessageBody(e));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(generateMessageBody(e));
    }

    private static Message generateMessageBody(Throwable exception) {
        return Message.builder()
                .exception(exception.getClass().getName())
                .message(String.format("%s", exception.getMessage()))
                .cause(exception.getCause())
                .build();
    }
}