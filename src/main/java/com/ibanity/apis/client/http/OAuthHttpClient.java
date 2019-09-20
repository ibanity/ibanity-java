package com.ibanity.apis.client.http;

import java.net.URI;
import java.util.Map;

public interface OAuthHttpClient {

    String post(URI path, Map<String, String> arguments, String clientSecret);

    String post(URI path, Map<String, String> additionalHeaders, Map<String, String> arguments, String clientSecret);
}
