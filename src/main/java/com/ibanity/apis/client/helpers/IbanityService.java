package com.ibanity.apis.client.helpers;

import com.ibanity.apis.client.holders.CertificateHolder;
import com.ibanity.apis.client.holders.SignatureCertificateHolder;
import com.ibanity.apis.client.network.http.client.IbanityHttpClient;
import com.ibanity.apis.client.network.http.client.factory.IbanityHttpClientFactory;
import com.ibanity.apis.client.services.ApiUrlProvider;
import com.ibanity.apis.client.services.impl.ApiUrlProviderImpl;

import java.security.cert.Certificate;

public class IbanityService {

    private final Certificate caCertificate;
    private final CertificateHolder applicationCertificate;
    private final SignatureCertificateHolder signatureCertificate;

    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public IbanityService(String apiEndpoint, Certificate caCertificate, CertificateHolder applicationCertificate, SignatureCertificateHolder signatureCertificate) {
        this.caCertificate = caCertificate;
        this.applicationCertificate = applicationCertificate;
        this.signatureCertificate = signatureCertificate;
        this.ibanityHttpClient = new IbanityHttpClientFactory().create(caCertificate, applicationCertificate, signatureCertificate);
        this.apiUrlProvider = new ApiUrlProviderImpl(ibanityHttpClient, apiEndpoint);
    }

    public ApiUrlProvider apiUrlProvider() {
        return apiUrlProvider;
    }

    public IbanityHttpClient ibanityHttpClient() {
        return ibanityHttpClient;
    }
}
