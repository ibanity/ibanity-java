package com.ibanity.apis.client.exceptions;

public class SignatureException extends Exception {
    public SignatureException() {
        super();
    }

    public SignatureException(final String message) {
        super(message);
    }

    public SignatureException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public SignatureException(final Throwable cause) {
        super(cause);
    }
}
