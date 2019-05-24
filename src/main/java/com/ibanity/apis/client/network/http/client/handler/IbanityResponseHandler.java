package com.ibanity.apis.client.network.http.client.handler;

import com.ibanity.apis.client.exceptions.IbanityClientSideException;
import com.ibanity.apis.client.exceptions.IbanityServerSideException;
import com.ibanity.apis.client.jsonapi.ErrorResourceApiModel;
import com.ibanity.apis.client.models.IbanityError;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;

import java.io.IOException;
import java.util.List;

import static com.ibanity.apis.client.network.http.client.IbanityHttpUtils.objectMapper;

public class IbanityResponseHandler implements ResponseHandler<String> {

    private static final int CLIENT_ERROR = 400;
    private static final int SERVER_ERROR = 500;
    private static final String DEFAULT_ENCODING = "UTF-8";

    @Override
    public String handleResponse(HttpResponse httpResponse) throws IOException {
        if (httpResponse.getStatusLine().getStatusCode() >= SERVER_ERROR) {
            throw new IbanityServerSideException(parseErrors(httpResponse));
        } else if (httpResponse.getStatusLine().getStatusCode() >= CLIENT_ERROR) {
            throw new IbanityClientSideException(parseErrors(httpResponse));
        }

        return readResponseContent(httpResponse.getEntity());
    }

    private List<IbanityError> parseErrors(HttpResponse httpResponse) {
        try {
            String payload = readResponseContent(httpResponse.getEntity());
            return objectMapper().readValue(payload, ErrorResourceApiModel.class).getErrors();
        } catch (Exception e) {
            throw new RuntimeException("Invalid payload", e);
        }
    }

    private static String readResponseContent(HttpEntity entity) throws IOException {
        return IOUtils.toString(entity.getContent(), DEFAULT_ENCODING);
    }
}
