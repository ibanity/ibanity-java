package com.ibanity.apis.client.exceptions;

public class IbanityException extends RuntimeException {

    public IbanityException(String message) {
        super(message);
    }

    public IbanityException(String message, Throwable cause) {
        super(message, cause);
    }
}
