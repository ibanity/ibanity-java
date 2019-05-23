package com.ibanity.apis.client.network.http.client.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibanity.apis.client.models.IbanityModel;
import com.ibanity.apis.client.network.http.client.IbanityHttpClient;
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
import java.util.Collections;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static com.ibanity.apis.client.network.http.client.IbanityHttpUtils.objectMapper;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

public class IbanityHttpClientImpl implements IbanityHttpClient {

    private static final String DEFAULT_ENCODING = "UTF-8";

    private final HttpClient httpClient;

    public IbanityHttpClientImpl(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public String get(URI path) {
        return get(path, null, newHashMap());
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
            return IbanityJsonApiHelper.readResponseContent(httpClient.execute(httpGet).getEntity());
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while connecting to Ibanity", e);
        }
    }

    @Override
    public String post(URI path, IbanityModel ibanityModel) {
        return post(path, ibanityModel, null, newHashMap());
    }

    @Override
    public String post(URI path, IbanityModel ibanityModel, String customerAccessToken) {
        return post(path, ibanityModel, customerAccessToken, newHashMap());
    }

    @Override
    public String post(URI path, IbanityModel ibanityModel, String customerAccessToken, Map<String, String> additionalHeaders) {
        try {
            HttpPost httpPost = new HttpPost(path);
            addHeaders(customerAccessToken, additionalHeaders, httpPost);
            httpPost.setEntity(createEntityRequest(objectMapper().writeValueAsString(ibanityModel)));
            return readResponseContent(httpClient.execute(httpPost).getEntity());
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while connecting to Ibanity", e);
        }
    }

    @Override
    public String delete(URI path) {
        return delete(path, null, Collections.emptyMap());
    }

    @Override
    public String delete(URI path, String customerAccessToken) {
        return delete(path, customerAccessToken, Collections.emptyMap());
    }

    @Override
    public String delete(URI path, String customerAccessToken, Map<String, String> additionalHeaders) {
        try {
            HttpDelete httpDelete = new HttpDelete(path);
            addHeaders(customerAccessToken, additionalHeaders, httpDelete);
            return readResponseContent(httpClient.execute(httpDelete).getEntity());
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while connecting to Ibanity", e);
        }
    }

    private HttpEntity createEntityRequest(String baseRequest) throws JsonProcessingException {
        return new StringEntity(baseRequest, APPLICATION_JSON);
    }

    private String readResponseContent(HttpEntity entity) throws IOException {
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
