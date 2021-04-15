package gr.uoa.di.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)  // 401
public class AuthorizationException extends RuntimeException{

    public AuthorizationException(String message) { super(message);}

    public AuthorizationException(String message, Throwable err) { super(message, err);}

    public HttpStatus getStatus() {
        return HttpStatus.UNPROCESSABLE_ENTITY;
    }
}
