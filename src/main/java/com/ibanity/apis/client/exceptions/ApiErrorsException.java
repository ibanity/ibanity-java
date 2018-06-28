package com.ibanity.apis.client.exceptions;

import io.crnk.core.engine.document.ErrorData;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ApiErrorsException extends IbanityException {
    private final List<ErrorData> errorDatas;
    private final int httpStatus;


    public ApiErrorsException(final int httpStatus, final List<ErrorData> errorDatas) {
        super(errorDatas.stream().map(errorData -> errorData.getCode()).collect(Collectors.joining(":")));
        this.errorDatas = errorDatas;
        this.httpStatus = httpStatus;
    }

    public ApiErrorsException(final int httpStatus, final ErrorData errorData, final Throwable e) {
        super(errorData.getCode(), e);
        this.errorDatas = new ArrayList<>();
        this.errorDatas.add(errorData);
        this.httpStatus = httpStatus;
    }

    public List<ErrorData> getErrorDatas() {
        return errorDatas;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("cause", getCause())
                .append("errorDatas", errorDatas)
                .append("fillInStackTrace", fillInStackTrace())
                .append("httpStatus", httpStatus)
                .append("localizedMessage", getLocalizedMessage())
                .append("message", getMessage())
                .append("stackTrace", getStackTrace())
                .append("suppressed", getSuppressed())
                .toString();
    }
}
