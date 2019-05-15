package com.ibanity.apis.client.network.http.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.jsonapi.ResourceApiModel;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

public class IbanityHttpClientImpl<T> implements IbanityHttpClient<T> {

    private static final String DEFAULT_ENCODING = "UTF-8";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public IbanityHttpClientImpl(HttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public String get(URI path, String customerAccessToken) {
        return get(path, customerAccessToken, newHashMap());
    }

    @Override
    public String get(URI path, String customerAccessToken, Map<String, String> additionalHeaders) {
        try {
            HttpGet httpGet = new HttpGet(path);
            addHeaders(customerAccessToken, additionalHeaders, httpGet);
            return toString(httpClient.execute(httpGet).getEntity());
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while connecting to Ibanity", e);
        }
    }

    @Override
    public String post(URI path, String customerAccessToken, Map<String, String> additionalHeaders, T payload) {
        try {
            HttpPost httpPost = new HttpPost(path);
            addHeaders(customerAccessToken, additionalHeaders, httpPost);
            httpPost.setEntity(createEntityRequest(payload));
            return toString(httpClient.execute(httpPost).getEntity());
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while connecting to Ibanity", e);
        }
    }

    @Override
    public String delete(URI path, String customerAccessToken, Map<String, String> additionalHeaders) {
        try {
            HttpDelete httpDelete = new HttpDelete(path);
            addHeaders(customerAccessToken, additionalHeaders, httpDelete);
            return toString(httpClient.execute(httpDelete).getEntity());
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while connecting to Ibanity", e);
        }
    }

    private HttpEntity createEntityRequest(T payload) throws JsonProcessingException {
        RequestApiModel<T> baseRequest = RequestApiModel.<T>builder().data(
                ResourceApiModel.<T>builder()
                        .type("")
                        .attributes(payload)
                        .build())
                .build();
        return new StringEntity(toJson(baseRequest), APPLICATION_JSON);
    }

    private String toJson(RequestApiModel baseRequest) throws JsonProcessingException {
        return objectMapper.writeValueAsString(baseRequest);
    }

    private String toString(HttpEntity entity) throws IOException {
        return IOUtils.toString(entity.getContent(), DEFAULT_ENCODING);
    }

    private void addHeaders(String customerAccessToken, Map<String, String> additionalHeaders, HttpRequestBase httpRequestBase) {
        addAuthorizationHeader(customerAccessToken, httpRequestBase);
        additionalHeaders.forEach(httpRequestBase::addHeader);
    }

    private void addAuthorizationHeader(String customerAccessToken, HttpRequestBase post) {
        if (StringUtils.isNotBlank(customerAccessToken)) {
            post.addHeader(new BasicHeader(AUTHORIZATION, "Bearer " + customerAccessToken));
        }
    }
}
