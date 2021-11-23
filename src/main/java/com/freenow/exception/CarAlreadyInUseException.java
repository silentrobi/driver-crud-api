package com.freenow.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Car already in use.")
public class CarAlreadyInUseException extends Exception {
    static final long serialVersionUID = -3387516993224229948L;

    public CarAlreadyInUseException(String message) {
        super(message);
    }
}
