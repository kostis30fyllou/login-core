package gr.uoa.di.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)  // 404
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable err) {
        super(message, err);
    }

    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }

}
