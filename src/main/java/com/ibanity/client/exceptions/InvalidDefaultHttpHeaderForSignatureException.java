package com.ibanity.client.exceptions;

public class InvalidDefaultHttpHeaderForSignatureException extends Exception {
    public InvalidDefaultHttpHeaderForSignatureException() {
        super();
    }

    public InvalidDefaultHttpHeaderForSignatureException(String message) {
        super(message);
    }

    public InvalidDefaultHttpHeaderForSignatureException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidDefaultHttpHeaderForSignatureException(Throwable cause) {
        super(cause);
    }
}
