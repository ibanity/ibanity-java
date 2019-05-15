package com.ibanity.apis.client.services.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.network.http.client.IbanityHttpClient;
import com.ibanity.apis.client.network.http.client.IbanityHttpUtils;
import com.ibanity.apis.client.services.ApiUrlProvider;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.stream.Stream;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.removeEnd;

public class ApiUrlProviderImpl implements ApiUrlProvider {

    private JsonNode apiUrls;
    private final IbanityHttpClient ibanityHttpClient;

    public ApiUrlProviderImpl(IbanityHttpClient ibanityHttpClient) {
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public String find(String... paths) {
        if (apiUrls == null) {
            throw new IllegalStateException("Api schema hasn't been loaded");
        }
        try {
            return Stream.of(paths)
                    .reduce(apiUrls, JsonNode::get, (jsonNode1, jsonNode2) -> jsonNode2)
                    .textValue();
        } catch (Exception e) {
            throw new IllegalArgumentException("Url cannot be found");
        }
    }

    @Override
    public void loadApiSchema() {
        String ibanityApiUrl = removeEnd(IbanityConfiguration.getConfiguration(IbanityConfiguration.IBANITY_API_ENDPOINT_PROPERTY_KEY), "/");
        try {
            String schema = ibanityHttpClient.get(new URI(ibanityApiUrl + "/xs2a"), null);
            apiUrls = mapJsonToMap(schema);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(format("Cannot create api schema URI for string %s", ibanityApiUrl), e);
        } catch (IOException e) {
            throw new IllegalArgumentException("Cannot parse api schema", e);
        }
    }

    private JsonNode mapJsonToMap(String schema) throws IOException {
        return IbanityHttpUtils.objectMapper().readTree(schema).get("links");
    }
}
