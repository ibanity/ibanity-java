package com.ibanity.apis.client.services.impl;

import com.ibanity.apis.client.configuration.ApiUrls;
import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.exceptions.IbanityException;
import com.ibanity.apis.client.network.http.client.IbanityHttpAdapterListener;
import com.ibanity.apis.client.services.ApiService;
import io.crnk.client.CrnkClient;
import io.crnk.client.http.HttpAdapterRequest;
import io.crnk.client.http.HttpAdapterResponse;
import io.crnk.client.http.apache.HttpClientAdapter;
import io.crnk.client.response.JsonLinksInformation;
import io.crnk.core.engine.document.Document;
import io.crnk.core.engine.http.HttpMethod;

import java.io.IOException;

import static com.ibanity.apis.client.configuration.IbanityConfiguration.getConfiguration;

public class ApiServiceImpl extends AbstractServiceImpl implements ApiService {

    private static final String IBANITY_API_ENDPOINT =
            getConfiguration(IbanityConfiguration.IBANITY_API_ENDPOINT_PROPERTY_KEY);

    @Override
    public ApiUrls getApiUrls() {
        CrnkClient apiClient = getApiClient(IBANITY_API_ENDPOINT);

        HttpClientAdapter httpClientAdapter = (HttpClientAdapter) apiClient.getHttpAdapter();
        httpClientAdapter.addListener(new IbanityHttpAdapterListener());

        HttpAdapterRequest httpAdapterRequest = httpClientAdapter.newRequest(
                apiClient.getServiceUrlProvider().getUrl(), HttpMethod.GET, "");

        try {
            HttpAdapterResponse httpAdapterResponse = httpAdapterRequest.execute();
            if (!httpAdapterResponse.isSuccessful()) {
                throw new IbanityException("Failed to list Ibanity API URLs: " + httpAdapterResponse.body());
            }

            Document document = apiClient.getObjectMapper().readValue(httpAdapterResponse.body(), Document.class);
            if (document.getLinks() == null) {
                throw new IbanityException("Failed to list Ibanity API URLs: no 'links' in the response");
            }

            return new JsonLinksInformation(document.getLinks(), apiClient.getObjectMapper())
                    .as(ApiUrls.class);
        } catch (IOException exception) {
            throw new IbanityException(exception.getMessage(), exception);
        }
    }
}
