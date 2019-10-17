package com.ibanity.apis.client.http.handler;

import com.ibanity.apis.client.exceptions.IbanityClientException;
import com.ibanity.apis.client.exceptions.IbanityServerException;
import com.ibanity.apis.client.jsonapi.ErrorResourceApiModel;
import com.ibanity.apis.client.models.IbanityError;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;

import java.io.IOException;
import java.util.List;

import static com.ibanity.apis.client.utils.IbanityUtils.objectMapper;

public class IbanityResponseHandler implements ResponseHandler<String> {

    private static final int CLIENT_ERROR = 400;
    private static final int SERVER_ERROR = 500;
    private static final String DEFAULT_ENCODING = "UTF-8";

    @Override
    public String handleResponse(HttpResponse httpResponse) throws IOException {
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode >= SERVER_ERROR) {
            throw new IbanityServerException(parseErrors(httpResponse), statusCode);
        } else if (statusCode >= CLIENT_ERROR) {
            throw new IbanityClientException(parseErrors(httpResponse), statusCode);
        }

        return readResponseContent(httpResponse.getEntity());
    }

    private List<IbanityError> parseErrors(HttpResponse httpResponse) {
        try {
            String payload = readResponseContent(httpResponse.getEntity());
            return objectMapper().readValue(payload, ErrorResourceApiModel.class).getErrors();
        } catch (Exception exception) {
            throw new RuntimeException("Invalid payload", exception);
        }
    }

    private static String readResponseContent(HttpEntity entity) throws IOException {
        return IOUtils.toString(entity.getContent(), DEFAULT_ENCODING);
    }
}
