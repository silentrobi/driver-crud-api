package com.freenow.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid credential.")
public class InvalidCredentialException extends Exception {

    static final long serialVersionUID = -3387516993224229948L;

    public InvalidCredentialException(String message) {
        super(message);
    }
}
