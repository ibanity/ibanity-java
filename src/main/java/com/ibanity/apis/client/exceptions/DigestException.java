package com.ibanity.apis.client.exceptions;

public class DigestException extends Exception {
    public DigestException() {
        super();
    }

    public DigestException(final String message) {
        super(message);
    }

    public DigestException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DigestException(final Throwable cause) {
        super(cause);
    }
}
