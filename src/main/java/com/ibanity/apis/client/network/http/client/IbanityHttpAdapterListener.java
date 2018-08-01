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
import java.util.UUID;

public class IbanityHttpAdapterListener implements HttpClientAdapterListener {
    private static final Logger LOGGER = LogManager.getLogger(IbanityHttpAdapterListener.class);
    private static final int DEFAULT_REQUEST_TIMEOUT = 10_000;

    private String customerAccessToken;
    private final UUID idempotencyKey;
    private SSLContext sslContext;

    public IbanityHttpAdapterListener() {
        LOGGER.info("Configured IbanityHttpAdapterListener WITHOUT customerAccessToken and idempotencyKey");
        this.idempotencyKey = UUID.randomUUID();
    }

    public IbanityHttpAdapterListener(final String customerAccessToken, final UUID idempotencyKey) {
        this.customerAccessToken = customerAccessToken;
        this.idempotencyKey = idempotencyKey;

        LOGGER.info(
                String.format("Configured IbanityHttpAdapterListener WITH customerAccessToken %s and idempotencyKey %s",
                        this.customerAccessToken, this.idempotencyKey));

        try {
            this.sslContext = IbanityHttpUtils.getSSLContext();
        } catch (GeneralSecurityException | IOException e) {
            throw new IllegalArgumentException("Unable to instantiate SSLContext with the given properties "
                    + "(certificate, private key, etc) " + e.getMessage(), e);
        }
    }

    @Override
    public void onBuild(final HttpClientBuilder httpClientBuilder) {
        try {
            this.sslContext = IbanityHttpUtils.getSSLContext();
        } catch (GeneralSecurityException | IOException e) {
            throw new IllegalArgumentException("Unable to instantiate SSLContext with the given properties "
                    + "(certificate, private key, etc) " + e.getMessage(), e);
        }

        httpClientBuilder.setSSLContext(sslContext);
        httpClientBuilder.setSSLSocketFactory(new SSLConnectionSocketFactory(sslContext));
        httpClientBuilder.setDefaultHeaders(getAuthorizationHttpRequestHeaders());
        httpClientBuilder.setRetryHandler(new CustomHttpRequestRetryHandler(10, true));
        httpClientBuilder.addInterceptorLast(new IdempotencyInterceptor());
        if (customerAccessToken != null) {
            httpClientBuilder.addInterceptorLast(new IbanitySignatureInterceptor());
        }
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(DEFAULT_REQUEST_TIMEOUT)
                .setSocketTimeout(DEFAULT_REQUEST_TIMEOUT)
                .setConnectionRequestTimeout(DEFAULT_REQUEST_TIMEOUT)
                .build();
        httpClientBuilder.setDefaultRequestConfig(requestConfig);
        httpClientBuilder.setConnectionReuseStrategy(new DefaultClientConnectionReuseStrategy());
    }

    private Collection<Header> getAuthorizationHttpRequestHeaders() {
        Collection<Header> authorizationHttpRequestHeaders = new ArrayList();
        if (customerAccessToken != null) {
            authorizationHttpRequestHeaders.add(new BasicHeader(HttpHeaders.AUTHORIZATION, "Bearer " + customerAccessToken));
        }
        return authorizationHttpRequestHeaders;
    }
}
