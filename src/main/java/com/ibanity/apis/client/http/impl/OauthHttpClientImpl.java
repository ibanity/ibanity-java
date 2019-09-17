package com.ibanity.apis.client.http.impl;

import com.ibanity.apis.client.http.OauthHttpClient;
import com.ibanity.apis.client.http.handler.IbanityResponseHandler;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.net.URI;
import java.util.Base64;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.collect.Maps.newHashMap;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.http.HttpHeaders.AUTHORIZATION;

public class OauthHttpClientImpl implements OauthHttpClient {

    private final String clientId;
    private final HttpClient httpClient;
    private final IbanityResponseHandler ibanityResponseHandler;

    public OauthHttpClientImpl(String clientId, HttpClient httpClient) {
        this.clientId = clientId;
        this.httpClient = httpClient;
        this.ibanityResponseHandler = new IbanityResponseHandler();
    }

    @Override
    public String post(URI path, Map<String, String> arguments, String clientSecret) {
        return post(path, newHashMap(), arguments, clientSecret);
    }

    @Override
    public String post(URI path, Map<String, String> additionalHeaders, Map<String, String> arguments, String clientSecret) {
        HttpPost post = new HttpPost(path);
        arguments.put("client_id", clientId);
        post.setEntity(createEntity(arguments));
        return execute(additionalHeaders, clientSecret, post);
    }

    private HttpEntity createEntity(Map<String, String> arguments) {
        return new UrlEncodedFormEntity(
                arguments.entrySet().stream()
                        .map(a -> new BasicNameValuePair(a.getKey(), a.getValue()))
                        .collect(Collectors.toList()),
                UTF_8);
    }

    private String execute(Map<String, String> additionalHeaders, String clientSecret, HttpRequestBase httpRequestBase) {
        try {
            addHeaders(clientSecret, additionalHeaders, httpRequestBase);
            return httpClient.execute(httpRequestBase, ibanityResponseHandler);
        } catch (IOException exception) {
            throw new RuntimeException("An error occurred while connecting to Ibanity", exception);
        }
    }

    private void addHeaders(String clientSecret, Map<String, String> additionalHeaders, HttpRequestBase httpRequestBase) {
        addAuthorizationHeader(clientId, clientSecret, httpRequestBase);
        additionalHeaders.forEach(httpRequestBase::addHeader);
    }

    private void addAuthorizationHeader(String clientId, String clientSecret, HttpRequestBase requestBase) {
        if(isNotBlank(clientId) && isNotBlank(clientSecret)) {
            String base64Encoded = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes(UTF_8));
            requestBase.addHeader(new BasicHeader(AUTHORIZATION, "Basic " + base64Encoded));
        }
    }
}
