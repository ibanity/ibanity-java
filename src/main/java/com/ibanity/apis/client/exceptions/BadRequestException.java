package com.ibanity.apis.client.exceptions;

public class BadRequestException extends io.crnk.core.exception.BadRequestException {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
