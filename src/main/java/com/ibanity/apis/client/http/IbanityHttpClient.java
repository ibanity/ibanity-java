package com.ibanity.apis.client.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;

import javax.net.ssl.SSLContext;
import java.net.URI;
import java.util.Map;

public interface IbanityHttpClient {

    String DEFAULT_ENCODING = "UTF-8";

    HttpResponse get(URI path);

    HttpResponse get(URI path, String customerAccessToken);

    HttpResponse get(URI path, Map<String, String> additionalHeaders, String customerAccessToken);

    HttpResponse post(URI path, Object requestApiModel);

    HttpResponse post(URI path, Object requestApiModel, String customerAccessToken);

    HttpResponse post(URI path, Object requestApiModel, Map<String, String> additionalHeaders, String customerAccessToken);

    HttpResponse delete(URI path);

    HttpResponse delete(URI path, String customerAccessToken);

    HttpResponse delete(URI path, Map<String, String> additionalHeaders, String customerAccessToken);

    HttpResponse patch(URI path, Object requestApiModel);

    HttpResponse patch(URI path, Object requestApiModel, String customerAccessToken);

    HttpResponse patch(URI path, Object requestApiModel, Map<String, String> additionalHeaders, String customerAccessToken);

    HttpClient httpClient();

    SSLContext sslContext();
}
