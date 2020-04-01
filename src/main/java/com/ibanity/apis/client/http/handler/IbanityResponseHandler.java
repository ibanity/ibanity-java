package com.ibanity.apis.client.http.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibanity.apis.client.exceptions.IbanityClientException;
import com.ibanity.apis.client.exceptions.IbanityServerException;
import com.ibanity.apis.client.jsonapi.ErrorResourceApiModel;
import com.ibanity.apis.client.jsonapi.FinancialInstitutionResponseApiModel;
import com.ibanity.apis.client.jsonapi.IbanityErrorApiModel;
import com.ibanity.apis.client.models.ErrorMeta;
import com.ibanity.apis.client.models.FinancialInstitutionResponse;
import com.ibanity.apis.client.models.IbanityError;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;

import java.util.List;
import java.util.stream.Collectors;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.readResponseContent;
import static com.ibanity.apis.client.utils.IbanityUtils.objectMapper;

public class IbanityResponseHandler implements ResponseHandler<HttpResponse> {

    private static final int CLIENT_ERROR = 400;
    private static final int SERVER_ERROR = 500;
    public static final String IBANITY_REQUEST_ID_HEADER = "ibanity-request-id";

    @Override
    public HttpResponse handleResponse(HttpResponse httpResponse) {
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode >= SERVER_ERROR) {
            throw new IbanityServerException(parseErrors(httpResponse), statusCode, getIbanityRequestId(httpResponse));
        } else if (statusCode >= CLIENT_ERROR) {
            throw new IbanityClientException(parseErrors(httpResponse), statusCode, getIbanityRequestId(httpResponse));
        }

        return httpResponse;
    }

    private String getIbanityRequestId(HttpResponse httpResponse) {
        Header firstHeader = httpResponse.getFirstHeader(IBANITY_REQUEST_ID_HEADER);
        return firstHeader != null ? firstHeader.getValue() : null;
    }

    private List<IbanityError> parseErrors(HttpResponse httpResponse) {
        try {
            String payload = readResponseContent(httpResponse.getEntity());
            List<IbanityErrorApiModel> errors = objectMapper().readValue(payload, ErrorResourceApiModel.class).getErrors();
            return errors.stream()
                    .map(this::mapError)
                    .collect(Collectors.toList());
        } catch (Exception exception) {
            throw new RuntimeException("Invalid payload", exception);
        }
    }

    private IbanityError mapError(IbanityErrorApiModel ibanityErrorApiModel) {
        IbanityError.IbanityErrorBuilder ibanityErrorBuilder = IbanityError.builder()
                .code(ibanityErrorApiModel.getCode())
                .detail(ibanityErrorApiModel.getDetail());
        if(ibanityErrorApiModel.getMeta() != null) {
            ErrorMeta.ErrorMetaBuilder errorMetaBuilder = ErrorMeta.builder();
            FinancialInstitutionResponseApiModel financialInstitutionResponseApiModel = ibanityErrorApiModel.getMeta().getFinancialInstitutionResponse();
            if(financialInstitutionResponseApiModel != null) {
                FinancialInstitutionResponse financialInstitutionResponse = FinancialInstitutionResponse.builder()
                        .requestUri(financialInstitutionResponseApiModel.getRequestUri())
                        .statusCode(financialInstitutionResponseApiModel.getStatusCode())
                        .body(parseBody(financialInstitutionResponseApiModel))
                        .requestId(financialInstitutionResponseApiModel.getRequestId())
                        .timestamp(financialInstitutionResponseApiModel.getTimestamp())
                        .build();
                errorMetaBuilder.financialInstitutionResponse(financialInstitutionResponse);
            }

            ibanityErrorBuilder
                    .meta(errorMetaBuilder.build());
        }
        return ibanityErrorBuilder
                .build();
    }

    private String parseBody(FinancialInstitutionResponseApiModel financialInstitutionResponseApiModel) {
        Object body = financialInstitutionResponseApiModel.getBody();
        if (body == null) {
            return null;
        } else if (body instanceof String) {
            return (String) body;
        } else {
            try {
                return objectMapper().writeValueAsString(body);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Invalid payload", e);
            }
        }
    }
}
