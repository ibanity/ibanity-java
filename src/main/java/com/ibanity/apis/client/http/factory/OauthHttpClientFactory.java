package com.ibanity.apis.client.http.factory;

import com.ibanity.apis.client.http.OAuthHttpClient;
import com.ibanity.apis.client.http.impl.OAuthHttpClientImpl;
import com.ibanity.apis.client.models.SignatureCredentials;
import com.ibanity.apis.client.models.TlsCredentials;
import com.ibanity.apis.client.utils.IbanityUtils;
import org.apache.http.client.HttpClient;

import java.security.cert.Certificate;

public class OauthHttpClientFactory {

    /**
     * @deprecated  Replaced by {@link #create(String, HttpClient)}
     */
    @Deprecated
    public OAuthHttpClient create(Certificate caCertificate,
                                  TlsCredentials tlsCredentials,
                                  SignatureCredentials signatureCertificate,
                                  String basePath,
                                  String clientId) {
        return new OAuthHttpClientImpl(clientId, IbanityUtils.httpClient(caCertificate, tlsCredentials, signatureCertificate, basePath));
    }

    public OAuthHttpClient create(String clientId, HttpClient httpClient) {
        return new OAuthHttpClientImpl(clientId, httpClient);
    }
}
