package com.ibanity.apis.client.http;

import org.apache.http.HttpResponse;

import java.net.URI;
import java.util.Map;

public interface OAuthHttpClient {

    HttpResponse post(URI path, Map<String, String> arguments, String clientSecret);

    HttpResponse post(URI path, Map<String, String> additionalHeaders, Map<String, String> arguments, String clientSecret);
}
