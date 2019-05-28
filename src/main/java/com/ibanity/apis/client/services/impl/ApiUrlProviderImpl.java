package com.ibanity.apis.client.services.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.network.http.client.IbanityHttpClient;
import com.ibanity.apis.client.network.http.client.IbanityHttpUtils;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.stream.Stream;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.removeEnd;

public class ApiUrlProviderImpl implements ApiUrlProvider {

    private static final Logger LOGGER = LogManager.getLogger(ApiUrlProviderImpl.class);
    private final String ibanityEndpoint;

    private JsonNode apiUrls;
    private final IbanityHttpClient ibanityHttpClient;

    public ApiUrlProviderImpl(IbanityHttpClient ibanityHttpClient, String ibanityEndpoint) {
        this.ibanityHttpClient = ibanityHttpClient;
        this.ibanityEndpoint = ibanityEndpoint;
    }

    @Override
    public String find(IbanityProduct ibanityProduct, String... paths) {
        if (apiUrls == null) {
            loadApiSchema();
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
        LOGGER.debug("loading schema");
        String ibanityApiUrl = removeEnd(ibanityEndpoint, "/");
        try {
            String schema = ibanityHttpClient.get(new URI(ibanityApiUrl + "/xs2a"), null);
            apiUrls = mapJsonToMap(schema);
            LOGGER.debug("schema loaded");
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
