package com.ibanity.apis.client.exceptions;

public class DigestException extends Exception {
    public DigestException() {
        super();
    }

    public DigestException(String message) {
        super(message);
    }

    public DigestException(String message, Throwable cause) {
        super(message, cause);
    }

    public DigestException(Throwable cause) {
        super(cause);
    }
}
