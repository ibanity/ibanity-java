package com.ibanity.client.exceptions;

public class IBanityException extends RuntimeException {

    public IBanityException(String message) {
        super(message);
    }

    public IBanityException(String message, Throwable cause) {
        super(message, cause);
    }
}
