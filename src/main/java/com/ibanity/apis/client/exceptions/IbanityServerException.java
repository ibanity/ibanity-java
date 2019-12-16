package com.ibanity.apis.client.exceptions;

import com.ibanity.apis.client.models.IbanityError;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class IbanityServerException extends IbanityException {

    public IbanityServerException(List<IbanityError> errors, int httpStatusCode, String requestId) {
        super(errors, httpStatusCode, requestId);
    }
}
