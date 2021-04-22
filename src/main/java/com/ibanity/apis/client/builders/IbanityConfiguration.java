package com.ibanity.apis.client.builders;


import com.ibanity.apis.client.models.SignatureCredentials;
import com.ibanity.apis.client.models.TlsCredentials;
import lombok.Builder;
import lombok.Getter;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;

import java.security.cert.Certificate;
import java.util.List;

@Getter
@Builder
public class IbanityConfiguration {

    private String apiEndpoint;
    private String proxyEndpoint;
    private String pontoConnectOauth2ClientId;
    private String isabelConnectOauth2ClientId;
    private TlsCredentials tlsCredentials;
    private SignatureCredentials signatureCredentials;
    private Certificate caCertificate;
    private List<HttpRequestInterceptor> httpRequestInterceptors;
    private List<HttpResponseInterceptor> httpResponseInterceptors;
    private int socketTimeout;
    private int connectTimeout;
    private int connectionRequestTimeout;

}
