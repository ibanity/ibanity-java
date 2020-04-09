package com.ibanity.apis.client.builders;

import com.ibanity.apis.client.services.IbanityService;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;

import java.security.PrivateKey;
import java.security.cert.Certificate;

public interface OptionalPropertiesBuilder {

    IbanityService build();

    OptionalPropertiesBuilder caCertificate(Certificate certificate);

    RequestSignaturePassphraseBuilder requestSignaturePrivateKey(PrivateKey privateKey);

    OptionalPropertiesBuilder pontoConnectOauth2ClientId(String clientId);

    OptionalPropertiesBuilder proxyEndpoint(String proxyEndpoint);

    OptionalPropertiesBuilder withHttpRequestInterceptor(HttpRequestInterceptor... httpRequestInterceptor);

    OptionalPropertiesBuilder withHttpResponseInterceptor(HttpResponseInterceptor... httpResponseInterceptor);
}
