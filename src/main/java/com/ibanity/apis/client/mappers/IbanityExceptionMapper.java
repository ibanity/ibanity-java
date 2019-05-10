package com.ibanity.apis.client.mappers;

import com.ibanity.apis.client.exceptions.ApiErrorsException;
import io.crnk.core.engine.document.ErrorData;
import io.crnk.core.engine.error.ErrorResponse;
import io.crnk.core.engine.error.ExceptionMapper;

import java.util.List;

public class IbanityExceptionMapper implements ExceptionMapper<ApiErrorsException> {
    private static final int CLIENT_ERROR_SERIE_VALUE = 4;
    private static final int SERVER_ERROR_SERIE_VALUE = 5;
    private static final int SERIES_DIVIDER = 100;

    @Override
    public ErrorResponse toErrorResponse(final ApiErrorsException exception) {
        return ErrorResponse.builder()
                .setStatus(exception.getHttpStatus())
                .setErrorData(exception.getErrorDatas())
                .build();
    }

    @Override
    public ApiErrorsException fromErrorResponse(final ErrorResponse errorResponse) {
        List<ErrorData> errors = (List<ErrorData>) errorResponse.getResponse().getEntity();

        return new ApiErrorsException(errorResponse.getHttpStatus(), errors);
    }

    @Override
    public boolean accepts(final ErrorResponse errorResponse) {
        int seriesCode = errorResponse.getHttpStatus() / SERIES_DIVIDER;

        return seriesCode == CLIENT_ERROR_SERIE_VALUE || seriesCode == SERVER_ERROR_SERIE_VALUE;
    }
}
