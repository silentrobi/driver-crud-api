package com.freenow.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Driver is offline.")
public class DriverOfflineException extends Exception
{
    static final long serialVersionUID = -3387516993224229948L;

    public DriverOfflineException(String message) {
        super(message);
    }
}
