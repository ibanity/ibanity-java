package com.ibanity.apis.client.network.http.client.factory;

import com.ibanity.apis.client.holders.CertificateHolder;
import com.ibanity.apis.client.holders.SignatureCertificateHolder;
import com.ibanity.apis.client.network.http.client.IbanityHttpClient;
import com.ibanity.apis.client.network.http.client.IbanityHttpUtils;
import com.ibanity.apis.client.network.http.client.impl.IbanityHttpClientImpl;

import java.security.cert.Certificate;

public class IbanityHttpClientFactory {

    public IbanityHttpClient create(Certificate caCertificate,
                                    CertificateHolder applicationCertificate,
                                    SignatureCertificateHolder signatureCertificate) {
        return new IbanityHttpClientImpl(IbanityHttpUtils.httpClient(caCertificate, applicationCertificate, signatureCertificate));
    }
}
