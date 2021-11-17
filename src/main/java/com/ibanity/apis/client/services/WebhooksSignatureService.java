package com.ibanity.apis.client.services;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

public interface WebhooksSignatureService {

    void verify(HttpResponse httpResponse);

    void verify(String payload, Header signatureHeader);

    void verify(String payload, String jwt);

    String keys();
}
