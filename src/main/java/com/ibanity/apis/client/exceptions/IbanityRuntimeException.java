package com.ibanity.apis.client.exceptions;

public class IbanityRuntimeException extends RuntimeException {

    public IbanityRuntimeException() {
    }

    public IbanityRuntimeException(String message, Exception e) {
        super(message, e);
    }

    public IbanityRuntimeException(String message) {
        super(message);
    }
}
