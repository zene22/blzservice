package info.furbach.blz.rest.blz.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.ToString;
import lombok.experimental.Tolerate;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Builder
@Getter
@ToString(exclude = {"cause", "fields"})
public final class Message implements Serializable {

    private static final long serialVersionUID = 9073996538023673062L;

    private String exception;
    private String message;
    private Throwable cause;

    @Singular("field")
    private Map<String, Object> fields = new HashMap<>();

    @Tolerate
    public Message() {
    }
}