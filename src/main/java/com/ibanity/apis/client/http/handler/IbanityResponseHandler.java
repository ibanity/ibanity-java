package com.ibanity.apis.client.http.handler;

import com.ibanity.apis.client.exceptions.IbanityClientException;
import com.ibanity.apis.client.exceptions.IbanityServerException;
import com.ibanity.apis.client.jsonapi.ErrorResourceApiModel;
import com.ibanity.apis.client.jsonapi.IbanityErrorApiModel;
import com.ibanity.apis.client.jsonapi.OAuth2ErrorResourceApiModel;
import com.ibanity.apis.client.mappers.IbanityErrorMapper;
import com.ibanity.apis.client.models.IbanityError;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;

import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;
import static com.ibanity.apis.client.mappers.ModelMapperHelper.readResponseContent;
import static com.ibanity.apis.client.utils.IbanityUtils.objectMapper;
import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

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
            if (!errors.isEmpty()) {
                return errors.stream()
                        .map(IbanityErrorMapper::map)
                        .collect(Collectors.toList());
            }

            OAuth2ErrorResourceApiModel oAuth2ErrorResourceApiModel = objectMapper().readValue(payload, OAuth2ErrorResourceApiModel.class);
            if (isNotBlank(oAuth2ErrorResourceApiModel.getError())) {
                return newArrayList(IbanityErrorMapper.map(oAuth2ErrorResourceApiModel));
            }

            return emptyList();
        } catch (Exception exception) {
            throw new RuntimeException("Invalid payload", exception);
        }
    }
}
