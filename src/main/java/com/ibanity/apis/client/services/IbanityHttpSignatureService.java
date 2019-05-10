package com.ibanity.apis.client.services;

import java.net.URL;
import java.util.Map;

public interface IbanityHttpSignatureService {

    Map<String, String> getHttpSignatureHeaders(String httpMethod, URL url, Map<String, String> requestHeaders);

    Map<String, String> getHttpSignatureHeaders(String httpMethod, URL url, Map<String, String> requestHeaders, String payload);

}
