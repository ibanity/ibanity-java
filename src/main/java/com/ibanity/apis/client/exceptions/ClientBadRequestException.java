package com.ibanity.apis.client.exceptions;

import io.crnk.core.exception.BadRequestException;

public class ClientBadRequestException extends BadRequestException {

    public ClientBadRequestException(final String message) {
        super(message);
    }

    public ClientBadRequestException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
