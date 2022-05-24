package com.ibanity.apis.client.http.impl;

import com.ibanity.apis.client.http.OAuthHttpClient;
import com.ibanity.apis.client.http.handler.IbanityResponseHandler;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.net.URI;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.http.HttpHeaders.AUTHORIZATION;

public class OAuthHttpClientImpl implements OAuthHttpClient {

    private final String clientId;
    private final HttpClient httpClient;
    private final IbanityResponseHandler ibanityResponseHandler;

    public OAuthHttpClientImpl(String clientId, HttpClient httpClient) {
        this.clientId = clientId;
        this.httpClient = httpClient;
        this.ibanityResponseHandler = new IbanityResponseHandler();
    }

    @Override
    public HttpResponse post(URI path, Map<String, String> arguments, String clientSecret) {
        return post(path, new HashMap<>(), arguments, clientSecret);
    }

    @Override
    public HttpResponse post(URI path, Map<String, String> additionalHeaders, Map<String, String> arguments, String clientSecret) {
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

    private HttpResponse execute(Map<String, String> additionalHeaders, String clientSecret, HttpRequestBase httpRequestBase) {
        try {
            addHeaders(clientSecret, additionalHeaders, httpRequestBase);
            return ibanityResponseHandler.handleResponse(httpClient.execute(httpRequestBase));
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
