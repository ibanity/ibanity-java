package com.ibanity.apis.client.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.http.factory.IbanityHttpClientFactory;
import com.ibanity.apis.client.models.SignatureCredentials;
import com.ibanity.apis.client.models.TlsCredentials;
import com.ibanity.apis.client.products.xs2a.services.Xs2aService;
import com.ibanity.apis.client.products.xs2a.services.impl.Xs2aServiceImpl;
import com.ibanity.apis.client.services.ApiUrlProvider;
import com.ibanity.apis.client.services.IbanityService;

import java.security.cert.Certificate;

public class IbanityServiceImpl implements IbanityService {

    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;
    private final Xs2aService xs2aService;

    public IbanityServiceImpl(String apiEndpoint,
                              Certificate caCertificate,
                              TlsCredentials tlsCredentials,
                              SignatureCredentials signatureCredentials) {
        this.ibanityHttpClient = new IbanityHttpClientFactory().create(caCertificate, tlsCredentials, signatureCredentials, apiEndpoint);
        this.apiUrlProvider = new ApiUrlProviderImpl(ibanityHttpClient, apiEndpoint);
        this.xs2aService = new Xs2aServiceImpl(apiUrlProvider, ibanityHttpClient);
    }

    public IbanityServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient, Xs2aService xs2aService) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
        this.xs2aService = xs2aService;
    }

    @Override
    public ApiUrlProvider apiUrlProvider() {
        return apiUrlProvider;
    }

    @Override
    public IbanityHttpClient ibanityHttpClient() {
        return ibanityHttpClient;
    }

    @Override
    public Xs2aService xs2aService() {
        return xs2aService;
    }
}
