package com.ibanity.apis.client.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibanity.apis.client.configuration.ApiIUrls;
import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.exceptions.IbanityException;
import com.ibanity.apis.client.services.ApiService;
import io.crnk.client.CrnkClient;
import io.crnk.client.http.HttpAdapterRequest;
import io.crnk.client.http.HttpAdapterResponse;
import io.crnk.client.response.JsonLinksInformation;
import io.crnk.core.engine.document.Document;
import io.crnk.core.engine.http.HttpMethod;

import java.io.IOException;

public class ApiServiceImpl extends AbstractServiceImpl implements ApiService {

    private static final String IBANITY_API_ENDPOINT = IbanityConfiguration.getConfiguration().getString(IbanityConfiguration.IBANITY_API_ENDPOINT_PROPERTY_KEY);

    @Override
    public ApiIUrls getApiUrls() {
        CrnkClient apiClient = getApiClient(IBANITY_API_ENDPOINT);
        HttpAdapterRequest httpAdapterRequest = apiClient.getHttpAdapter().newRequest(apiClient.getServiceUrlProvider().getUrl(), HttpMethod.GET, "");
        try {
            HttpAdapterResponse httpAdapterResponse = httpAdapterRequest.execute();
            if (!httpAdapterResponse.isSuccessful()) {
                throw new IbanityException("Impossible to get Ibanity list of APIs' URLs:" + httpAdapterResponse.body() + ":");
            }
            String body = httpAdapterResponse.body();
            ObjectMapper objectMapper = apiClient.getObjectMapper();
            Document document = (Document) objectMapper.readValue(body, Document.class);
            if (document.getLinks() != null) {
                return new JsonLinksInformation(document.getLinks(), objectMapper).as(ApiIUrls.class);
            } else {
                throw new IbanityException("Impossible to get Ibanity list of APIs' URLs: no links in the response.");
            }
        } catch (IOException e) {
            throw new IbanityException(e.getMessage(), e);
        }
    }
}
