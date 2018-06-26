package com.ibanity.apis.client.exceptions;

public class IbanityException extends RuntimeException {

    public IbanityException(final String message) {
        super(message);
    }

    public IbanityException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
