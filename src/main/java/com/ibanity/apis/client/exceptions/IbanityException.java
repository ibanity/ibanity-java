package com.ibanity.apis.client.exceptions;

import com.ibanity.apis.client.models.IbanityError;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
public abstract class IbanityException extends RuntimeException {

    private final List<IbanityError> errors;
    private final int httpStatusCode;

    public IbanityException(List<IbanityError> errors, int httpStatusCode) {
        super("Ibanity request failed.");
        this.errors = errors;
        this.httpStatusCode = httpStatusCode;
    }
}
