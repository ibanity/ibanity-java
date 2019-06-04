package com.ibanity.apis.client.http;

import java.net.URI;
import java.util.Map;

public interface IbanityHttpClient {

    String DEFAULT_ENCODING = "UTF-8";

    String get(URI path);

    String get(URI path, String customerAccessToken);

    String get(URI path, Map<String, String> additionalHeaders, String customerAccessToken);

    String post(URI path, Object requestApiModel);

    String post(URI path, Object requestApiModel, String customerAccessToken);

    String post(URI path, Object requestApiModel, Map<String, String> additionalHeaders, String customerAccessToken);

    String delete(URI path);

    String delete(URI path, String customerAccessToken);

    String delete(URI path, Map<String, String> additionalHeaders, String customerAccessToken);
}
