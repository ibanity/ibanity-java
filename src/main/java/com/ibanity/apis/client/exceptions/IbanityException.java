package com.ibanity.apis.client.exceptions;

import io.crnk.core.exception.CrnkException;

public class IbanityException extends CrnkException {
    public IbanityException(final String message) {
        super(message);
    }

    public IbanityException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
