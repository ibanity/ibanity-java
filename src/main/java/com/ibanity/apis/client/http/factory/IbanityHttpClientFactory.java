package com.ibanity.apis.client.http.factory;

import com.ibanity.apis.client.holders.ApplicationCertificateHolder;
import com.ibanity.apis.client.holders.SignatureCertificateHolder;
import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.http.IbanityHttpUtils;
import com.ibanity.apis.client.http.impl.IbanityHttpClientImpl;

import java.security.cert.Certificate;

public class IbanityHttpClientFactory {

    public IbanityHttpClient create(Certificate caCertificate,
                                    ApplicationCertificateHolder applicationCertificate,
                                    SignatureCertificateHolder signatureCertificate,
                                    String host) {
        return new IbanityHttpClientImpl(IbanityHttpUtils.httpClient(caCertificate, applicationCertificate, signatureCertificate, host));
    }
}
