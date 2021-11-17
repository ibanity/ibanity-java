package com.ibanity.apis.client.services.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.services.ApiUrlProvider;
import com.ibanity.apis.client.utils.IbanityUtils;
import org.apache.commons.lang3.StringUtils;
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
    private final String proxyEndpoint;
    private final IbanityHttpClient ibanityHttpClient;
    private final Map<String, JsonNode> apiUrls = new HashMap<>();

    public ApiUrlProviderImpl(IbanityHttpClient ibanityHttpClient, String ibanityEndpoint) {
        this.ibanityHttpClient = ibanityHttpClient;
        this.ibanityEndpoint = ibanityEndpoint;
        this.proxyEndpoint = null;
    }

    public ApiUrlProviderImpl(IbanityHttpClient ibanityHttpClient, String apiEndpoint, String proxyEndpoint) {
        this.ibanityHttpClient = ibanityHttpClient;
        this.ibanityEndpoint = apiEndpoint;
        this.proxyEndpoint = proxyEndpoint;
    }

    @Override
    public String find(IbanityProduct ibanityProduct, String... paths) {
        String productPath = ibanityProduct.path();
        return find(productPath, paths);
    }

    public String find(String rootPath, String[] subPaths) {
        try {
            JsonNode apiUrls = this.apiUrls.get(rootPath);

            if (apiUrls == null) {
                loadApiSchema(rootPath);
                apiUrls = this.apiUrls.get(rootPath);
            }

            return Stream.of(subPaths)
                    .reduce(apiUrls, JsonNode::get, (jsonNode1, jsonNode2) -> jsonNode2)
                    .textValue();
        } catch (Exception exception) {
            throw new IllegalArgumentException("Url cannot be found", exception);
        }
    }

    @Override
    public void loadApiSchema(IbanityProduct ibanityProduct) {
        String path = ibanityProduct.path();
        loadApiSchema(path);
    }

    private void loadApiSchema(String rootPath) {
        LOGGER.debug("loading schema for {}", rootPath);
        String ibanityApiUrl = removeEnd(targetUrl(), "/");
        try {
            HttpResponse httpResponse = ibanityHttpClient.get(new URI(ibanityApiUrl + "/" + rootPath), null);
            String schema = EntityUtils.toString(httpResponse.getEntity());
            if (useProxy()) {
                schema = schema.replace(ibanityEndpoint, proxyEndpoint);
            }

            JsonNode jsonNode = mapJsonToMap(schema);
            apiUrls.put(rootPath, jsonNode);
            LOGGER.debug("schema loaded");
        } catch (URISyntaxException exception) {
            throw new IllegalArgumentException(format("Cannot create api schema URI for string %s", ibanityApiUrl), exception);
        } catch (IOException exception) {
            throw new IllegalArgumentException("Cannot parse api schema", exception);
        }
    }

    private String targetUrl() {
        return useProxy() ? proxyEndpoint : ibanityEndpoint;
    }

    private boolean useProxy() {
        return StringUtils.isNotBlank(proxyEndpoint);
    }

    private JsonNode mapJsonToMap(String schema) throws IOException {
        return IbanityUtils.objectMapper().readTree(schema).get("links");
    }
}
