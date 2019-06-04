package com.ibanity.apis.client.exceptions;

import com.ibanity.apis.client.models.IbanityError;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class IbanityClientException extends IbanityException {

    public IbanityClientException(List<IbanityError> errors) {
       super(errors);
    }
}

