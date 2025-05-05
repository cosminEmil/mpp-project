package com.services;

public class ServicesException extends RuntimeException {

    public ServicesException() {}

    public ServicesException(String message) {
        super(message);
    }

    public ServicesException(String message, Throwable cause) {
        super(message, cause);
    }
}
