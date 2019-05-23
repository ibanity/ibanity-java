package com.ibanity.apis.client.network.http.client;

import com.ibanity.apis.client.models.IbanityModel;

import java.net.URI;
import java.util.Map;

public interface IbanityHttpClient {

    String get(URI path);

    String get(URI path, String customerAccessToken);

    String get(URI path, String customerAccessToken, Map<String, String> additionalHeaders);

    String post(URI path, IbanityModel ibanityModel);

    String post(URI path, IbanityModel ibanityModel, String customerAccessToken);

    String post(URI path, IbanityModel ibanityModel, String customerAccessToken, Map<String, String> additionalHeaders);

    String delete(URI path);

    String delete(URI path, String customerAccessToken);

    String delete(URI path, String customerAccessToken, Map<String, String> additionalHeaders);
}
