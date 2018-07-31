package com.ibanity.apis.client.mappers;

import com.ibanity.apis.client.exceptions.ApiErrorsException;
import io.crnk.core.engine.document.ErrorData;
import io.crnk.core.engine.error.ErrorResponse;
import io.crnk.core.engine.error.ExceptionMapper;

import java.util.List;

public class IbanityExceptionMapper implements ExceptionMapper<ApiErrorsException> {
    @Override
    public ErrorResponse toErrorResponse(final ApiErrorsException e) {
        return ErrorResponse.builder()
                .setStatus(e.getHttpStatus())
                .setErrorData(e.getErrorDatas())
                .build();
    }

    @Override
    public ApiErrorsException fromErrorResponse(final ErrorResponse errorResponse) {
        List<ErrorData> errors = (List<ErrorData>) errorResponse.getResponse().getEntity();

        return new ApiErrorsException(errorResponse.getHttpStatus(), errors);
    }

    @Override
    public boolean accepts(final ErrorResponse errorResponse) {
        return errorResponse.getHttpStatus() >= 400 & errorResponse.getHttpStatus() < 600;
    }
}
