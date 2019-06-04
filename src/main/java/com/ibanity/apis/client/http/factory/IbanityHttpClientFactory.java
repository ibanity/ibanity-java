package com.ibanity.apis.client.http.factory;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.http.impl.IbanityHttpClientImpl;
import com.ibanity.apis.client.models.ApplicationCredentials;
import com.ibanity.apis.client.models.SignatureCredentials;
import com.ibanity.apis.client.utils.IbanityUtils;

import java.security.cert.Certificate;

public class IbanityHttpClientFactory {

    public IbanityHttpClient create(Certificate caCertificate,
                                    ApplicationCredentials applicationCertificate,
                                    SignatureCredentials signatureCertificate,
                                    String basePath) {
        return new IbanityHttpClientImpl(IbanityUtils.httpClient(caCertificate, applicationCertificate, signatureCertificate, basePath));
    }
}
