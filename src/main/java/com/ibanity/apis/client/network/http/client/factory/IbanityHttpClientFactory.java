package com.ibanity.apis.client.network.http.client.factory;

import com.ibanity.apis.client.network.http.client.IbanityHttpClient;
import com.ibanity.apis.client.network.http.client.IbanityHttpUtils;
import com.ibanity.apis.client.network.http.client.impl.IbanityHttpClientImpl;

public class IbanityHttpClientFactory {

    public IbanityHttpClient create() {
        return new IbanityHttpClientImpl(IbanityHttpUtils.httpClient());
    }
}
