package com.ibanity.apis.client.http.factory;

import com.ibanity.apis.client.http.OauthHttpClient;
import com.ibanity.apis.client.http.impl.OauthHttpClientImpl;
import com.ibanity.apis.client.models.SignatureCredentials;
import com.ibanity.apis.client.models.TlsCredentials;
import com.ibanity.apis.client.utils.IbanityUtils;

import java.security.cert.Certificate;

public class OauthHttpClientFactory {

    public OauthHttpClient create(Certificate caCertificate,
                                  TlsCredentials tlsCredentials,
                                  SignatureCredentials signatureCertificate,
                                  String basePath,
                                  String clientId) {
        return new OauthHttpClientImpl(clientId, IbanityUtils.httpClient(caCertificate, tlsCredentials, signatureCertificate, basePath));
    }
}
