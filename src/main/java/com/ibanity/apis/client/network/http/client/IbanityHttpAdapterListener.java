package com.ibanity.apis.client.network.http.client;

import com.ibanity.apis.client.utils.CustomHttpRequestRetryHandler;
import io.crnk.client.http.apache.HttpClientAdapterListener;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.DefaultClientConnectionReuseStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collection;

public class IbanityHttpAdapterListener implements HttpClientAdapterListener {
    private static final Logger LOGGER = LogManager.getLogger(IbanityHttpAdapterListener.class);
    private static final int DEFAULT_REQUEST_TIMEOUT = 10_000;
    private static final int RETRY_COUNTS = 10;

    private String customerAccessToken;
    private SSLContext sslContext;

    public IbanityHttpAdapterListener() {
        LOGGER.info("Configured IbanityHttpAdapterListener WITHOUT customerAccessToken and idempotencyKey");
    }

    public IbanityHttpAdapterListener(final String customerAccessToken) {
        this.customerAccessToken = customerAccessToken;
        try {
            this.sslContext = IbanityHttpUtils.getSSLContext();
        } catch (GeneralSecurityException | IOException exception) {
            throw new IllegalArgumentException("Unable to instantiate SSLContext with the given properties "
                    + "(certificate, private key, etc) " + exception.getMessage(), exception);
        }
    }

    @Override
    public void onBuild(final HttpClientBuilder httpClientBuilder) {
        try {
            this.sslContext = IbanityHttpUtils.getSSLContext();
            IbanityHttpUtils.configureHttpClient(IbanityHttpUtils.getSSLContext(), httpClientBuilder);
        } catch (GeneralSecurityException | IOException exception) {
            throw new IllegalArgumentException("Unable to instantiate SSLContext with the given properties "
                    + "(certificate, private key, etc) " + exception.getMessage(), exception);
        }

        httpClientBuilder.setSSLContext(sslContext);
        httpClientBuilder.setSSLSocketFactory(new SSLConnectionSocketFactory(sslContext));
        httpClientBuilder.setDefaultHeaders(getAuthorizationHttpRequestHeaders());
        httpClientBuilder.setRetryHandler(new CustomHttpRequestRetryHandler(RETRY_COUNTS, true));
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(DEFAULT_REQUEST_TIMEOUT)
                .setSocketTimeout(DEFAULT_REQUEST_TIMEOUT)
                .setConnectionRequestTimeout(DEFAULT_REQUEST_TIMEOUT)
                .build();
        httpClientBuilder.setDefaultRequestConfig(requestConfig);
        httpClientBuilder.setConnectionReuseStrategy(new DefaultClientConnectionReuseStrategy());
    }

    private Collection<Header> getAuthorizationHttpRequestHeaders() {
        Collection<Header> authorizationHttpRequestHeaders = new ArrayList<>();
        if (customerAccessToken != null) {
            authorizationHttpRequestHeaders.add(new BasicHeader(HttpHeaders.AUTHORIZATION, "Bearer " + customerAccessToken));
        }
        return authorizationHttpRequestHeaders;
    }
}

