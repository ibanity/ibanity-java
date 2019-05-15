package com.ibanity.apis.client.network.http.client;

import io.crnk.client.http.apache.HttpClientAdapterListener;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class IbanityHttpAdapterListener implements HttpClientAdapterListener {

    @Override
    public void onBuild(final HttpClientBuilder httpClientBuilder) {
        try {
            IbanityHttpUtils.configureHttpClient(IbanityHttpUtils.getSSLContext(), httpClientBuilder);
        } catch (GeneralSecurityException | IOException exception) {
            throw new IllegalArgumentException("Unable to instantiate SSLContext with the given properties "
                    + "(certificate, private key, etc) " + exception.getMessage(), exception);
        }
    }
}
