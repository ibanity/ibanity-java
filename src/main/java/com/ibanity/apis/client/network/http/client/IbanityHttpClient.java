package com.ibanity.apis.client.network.http.client;

import com.ibanity.apis.client.models.IbanityModel;

import java.net.URI;
import java.util.Map;

public interface IbanityHttpClient {

    String get(URI path, String customerAccessToken);

    String get(URI path, String customerAccessToken, Map<String, String> additionalHeaders);

    String post(URI path, String customerAccessToken, IbanityModel ibanityModel);

    String post(URI path, String customerAccessToken, IbanityModel ibanityModel, Map<String, String> additionalHeaders);

    String delete(URI path, String customerAccessToken);

    String delete(URI path, String customerAccessToken, Map<String, String> additionalHeaders);
}
