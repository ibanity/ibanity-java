package com.ibanity.apis.client.exceptions;

public class InvalidDefaultHttpHeaderForSignatureException extends Exception {
    public InvalidDefaultHttpHeaderForSignatureException() {
        super();
    }

    public InvalidDefaultHttpHeaderForSignatureException(final String message) {
        super(message);
    }

    public InvalidDefaultHttpHeaderForSignatureException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public InvalidDefaultHttpHeaderForSignatureException(final Throwable cause) {
        super(cause);
    }
}
