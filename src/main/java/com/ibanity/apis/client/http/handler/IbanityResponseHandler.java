package com.ibanity.apis.client.http.handler;

import com.ibanity.apis.client.exceptions.IbanityClientException;
import com.ibanity.apis.client.exceptions.IbanityServerException;
import com.ibanity.apis.client.jsonapi.ErrorResourceApiModel;
import com.ibanity.apis.client.models.IbanityError;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;

import java.util.List;

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
            return objectMapper().readValue(payload, ErrorResourceApiModel.class).getErrors();
        } catch (Exception exception) {
            throw new RuntimeException("Invalid payload", exception);
        }
    }
}
