package gr.uoa.di.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)  // 409
public class ResourceConflictException extends RuntimeException {

    public ResourceConflictException() {
        super();
    }

    public ResourceConflictException(String message) {
        super(message);
    }

    public ResourceConflictException(String message, Throwable err) {
        super(message, err);
    }

    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}
