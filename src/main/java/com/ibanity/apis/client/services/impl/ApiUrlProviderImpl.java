package com.ibanity.apis.client.services.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.services.ApiUrlProvider;
import com.ibanity.apis.client.utils.IbanityUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.removeEnd;

public class ApiUrlProviderImpl implements ApiUrlProvider {

    private static final Logger LOGGER = LogManager.getLogger(ApiUrlProviderImpl.class);

    private final String ibanityEndpoint;
    private final IbanityHttpClient ibanityHttpClient;
    private final Map<IbanityProduct, JsonNode> apiUrls = new HashMap<>();

    public ApiUrlProviderImpl(IbanityHttpClient ibanityHttpClient, String ibanityEndpoint) {
        this.ibanityHttpClient = ibanityHttpClient;
        this.ibanityEndpoint = ibanityEndpoint;
    }

    @Override
    public String find(IbanityProduct ibanityProduct, String... paths) {
        try {
            JsonNode apiUrls = this.apiUrls.get(ibanityProduct);

            if (apiUrls == null) {
                loadApiSchema(ibanityProduct);
                apiUrls = this.apiUrls.get(ibanityProduct);
            }

            return Stream.of(paths)
                    .reduce(apiUrls, JsonNode::get, (jsonNode1, jsonNode2) -> jsonNode2)
                    .textValue();
        } catch (Exception exception) {
            throw new IllegalArgumentException("Url cannot be found", exception);
        }
    }

    @Override
    public void loadApiSchema(IbanityProduct ibanityProduct) {
        LOGGER.debug("loading schema for {}", ibanityProduct);
        String ibanityApiUrl = removeEnd(ibanityEndpoint, "/");
        try {
            HttpResponse httpResponse = ibanityHttpClient.get(new URI(ibanityApiUrl + "/" + ibanityProduct.path()), null);
            String schema = EntityUtils.toString(httpResponse.getEntity());
            JsonNode jsonNode = mapJsonToMap(schema);
            apiUrls.put(ibanityProduct, jsonNode);
            LOGGER.debug("schema loaded");
        } catch (URISyntaxException exception) {
            throw new IllegalArgumentException(format("Cannot create api schema URI for string %s", ibanityApiUrl), exception);
        } catch (IOException exception) {
            throw new IllegalArgumentException("Cannot parse api schema", exception);
        }
    }

    private JsonNode mapJsonToMap(String schema) throws IOException {
        return IbanityUtils.objectMapper().readTree(schema).get("links");
    }
}
