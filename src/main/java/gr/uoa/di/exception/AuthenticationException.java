package gr.uoa.di.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)  // 422
public class AuthenticationException extends RuntimeException{

    public AuthenticationException(String message) { super(message);}

    public AuthenticationException(String message, Throwable err) { super(message, err);}

    public HttpStatus getStatus() {
        return HttpStatus.UNPROCESSABLE_ENTITY;
    }
}
