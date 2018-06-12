package com.ibanity.apis.client.exceptions;

import io.crnk.core.exception.BadRequestException;

public class ClientBadRequestException extends BadRequestException {

    public ClientBadRequestException(String message) {
        super(message);
    }

    public ClientBadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
