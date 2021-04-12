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

    OptionalPropertiesBuilder withHttpRequestInterceptors(HttpRequestInterceptor... httpRequestInterceptor);

    OptionalPropertiesBuilder withHttpResponseInterceptors(HttpResponseInterceptor... httpResponseInterceptor);

    OptionalPropertiesBuilder socketTimeout(int socketTimeout);

    OptionalPropertiesBuilder connectTimeout(int connectTimeout);

    OptionalPropertiesBuilder connectionRequestTimeout(int connectionRequestTimeout);
}
