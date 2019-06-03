package com.ibanity.apis.client.helpers;

import com.ibanity.apis.client.holders.ApplicationCertificateHolder;
import com.ibanity.apis.client.holders.SignatureCertificateHolder;
import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.http.factory.IbanityHttpClientFactory;
import com.ibanity.apis.client.products.xs2a.services.Xs2aService;
import com.ibanity.apis.client.products.xs2a.services.impl.Xs2aServiceImpl;
import com.ibanity.apis.client.services.ApiUrlProvider;
import com.ibanity.apis.client.services.impl.ApiUrlProviderImpl;

import java.security.cert.Certificate;

public class IbanityService {

    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;
    private final Xs2aService xs2aService;

    public IbanityService(String apiEndpoint, Certificate caCertificate, ApplicationCertificateHolder applicationCertificate, SignatureCertificateHolder signatureCertificate) {
        this.ibanityHttpClient = new IbanityHttpClientFactory().create(caCertificate, applicationCertificate, signatureCertificate, apiEndpoint);
        this.apiUrlProvider = new ApiUrlProviderImpl(ibanityHttpClient, apiEndpoint);
        this.xs2aService = new Xs2aServiceImpl(apiUrlProvider, ibanityHttpClient);
    }

    public IbanityService(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient, Xs2aService xs2aService) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
        this.xs2aService = xs2aService;
    }

    public ApiUrlProvider apiUrlProvider() {
        return apiUrlProvider;
    }

    public IbanityHttpClient ibanityHttpClient() {
        return ibanityHttpClient;
    }

    public Xs2aService xs2aService() {
        return xs2aService;
    }
}
