package com.ibanity.apis.client.http.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.http.handler.IbanityResponseHandler;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static com.ibanity.apis.client.utils.IbanityUtils.objectMapper;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

public class IbanityHttpClientImpl implements IbanityHttpClient {

    private final HttpClient httpClient;
    private final SSLContext sslContext;
    private final IbanityResponseHandler ibanityResponseHandler;

    public IbanityHttpClientImpl(HttpClient httpClient, SSLContext sslContext) {
        this.httpClient = httpClient;
        this.sslContext = sslContext;
        ibanityResponseHandler = new IbanityResponseHandler();
    }

    @Override
    public SSLContext sslContext() {
        return sslContext;
    }

    @Override
    public HttpResponse get(@NonNull URI path) {
        return get(path, null);
    }

    @Override
    public HttpResponse get(@NonNull URI path, String customerAccessToken) {
        return get(path, newHashMap(), customerAccessToken);
    }

    @Override
    public HttpResponse get(@NonNull URI path, @NonNull Map<String, String> additionalHeaders, String customerAccessToken) {
        HttpGet httpGet = new HttpGet(path);
        return execute(additionalHeaders, customerAccessToken, httpGet);
    }

    @Override
    public HttpResponse post(@NonNull URI path, @NonNull Object requestApiModel) {
        return post(path, requestApiModel, null);
    }

    @Override
    public HttpResponse post(@NonNull URI path, @NonNull Object requestApiModel, String customerAccessToken) {
        return post(path, requestApiModel, newHashMap(), customerAccessToken);
    }

    @Override
    public HttpResponse post(@NonNull URI path, @NonNull Object requestApiModel, @NonNull Map<String, String> additionalHeaders, String customerAccessToken) {
        try {
            HttpPost httpPost = new HttpPost(path);
            httpPost.setEntity(createEntityRequest(objectMapper().writeValueAsString(requestApiModel)));
            return execute(additionalHeaders, customerAccessToken, httpPost);
        } catch (JsonProcessingException exception) {
            throw new RuntimeException("An error occurred while converting object to json", exception);
        }
    }

    @Override
    public HttpResponse delete(@NonNull URI path) {
        return delete(path, null);
    }

    @Override
    public HttpResponse delete(@NonNull URI path, String customerAccessToken) {
        return delete(path, Collections.emptyMap(), customerAccessToken);
    }

    @Override
    public HttpResponse delete(@NonNull URI path, @NonNull Map<String, String> additionalHeaders, String customerAccessToken) {
        HttpDelete httpDelete = new HttpDelete(path);
        return execute(additionalHeaders, customerAccessToken, httpDelete);
    }

    @Override
    public HttpResponse patch(@NonNull URI path, @NonNull Object requestApiModel) {
        return patch(path, requestApiModel, null);
    }

    @Override
    public HttpResponse patch(@NonNull URI path, @NonNull Object requestApiModel, String customerAccessToken) {
        return patch(path, requestApiModel, newHashMap(), customerAccessToken);
    }

    @Override
    public HttpResponse patch(@NonNull URI path, @NonNull Object requestApiModel, @NonNull Map<String, String> additionalHeaders, String customerAccessToken) {
        try {
            HttpPatch httpPatch = new HttpPatch(path);
            httpPatch.setEntity(createEntityRequest(objectMapper().writeValueAsString(requestApiModel)));
            return execute(additionalHeaders, customerAccessToken, httpPatch);
        } catch (JsonProcessingException exception) {
            throw new RuntimeException("An error occurred while converting object to json", exception);
        }
    }

    @Override
    public HttpClient httpClient() {
        return httpClient;
    }

    private HttpResponse execute(@NonNull Map<String, String> additionalHeaders, String customerAccessToken, HttpRequestBase httpRequestBase) {
        try {
            addHeaders(customerAccessToken, additionalHeaders, httpRequestBase);
            return ibanityResponseHandler.handleResponse(httpClient.execute(httpRequestBase));
        } catch (IOException exception) {
            throw new RuntimeException("An error occurred while connecting to Ibanity", exception);
        }
    }

    private HttpEntity createEntityRequest(String baseRequest) {
        return new StringEntity(baseRequest, APPLICATION_JSON);
    }

    private void addHeaders(String customerAccessToken, Map<String, String> additionalHeaders, HttpRequestBase httpRequestBase) {
        addAuthorizationHeader(customerAccessToken, httpRequestBase);
        additionalHeaders.forEach(httpRequestBase::addHeader);
    }

    private void addAuthorizationHeader(String customerAccessToken, HttpRequestBase requestBase) {
        if (StringUtils.isNotBlank(customerAccessToken)) {
            requestBase.addHeader(new BasicHeader(AUTHORIZATION, "Bearer " + customerAccessToken));
        }
    }
}
