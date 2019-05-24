package com.ibanity.apis.client.helpers;

import com.ibanity.apis.client.network.http.client.IbanityHttpClient;
import com.ibanity.apis.client.network.http.client.factory.IbanityHttpClientFactory;
import com.ibanity.apis.client.services.ApiUrlProvider;
import com.ibanity.apis.client.services.impl.ApiUrlProviderImpl;

public class IbanityService {

    private static IbanityHttpClient ibanityHttpClient = new IbanityHttpClientFactory().create();
    private static ApiUrlProvider apiUrlProvider = new ApiUrlProviderImpl(ibanityHttpClient);

    public static ApiUrlProvider apiUrlProvider() {
        return apiUrlProvider;
    }

    public static IbanityHttpClient ibanityHttpClient() {
        return ibanityHttpClient;
    }
}
