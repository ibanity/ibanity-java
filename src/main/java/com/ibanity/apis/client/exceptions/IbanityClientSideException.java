package com.ibanity.apis.client.exceptions;

import com.ibanity.apis.client.models.IbanityError;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class IbanityClientSideException extends IbanityException {

    public IbanityClientSideException(List<IbanityError> errors) {
       super(errors);
    }
}

