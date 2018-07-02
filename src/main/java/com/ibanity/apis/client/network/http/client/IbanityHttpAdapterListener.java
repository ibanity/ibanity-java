package com.ibanity.apis.client.network.http.client;

import io.crnk.client.http.apache.HttpClientAdapterListener;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class IbanityHttpAdapterListener implements HttpClientAdapterListener {

    private String customerAccessToken = null;
    private UUID idempotencyKey = null;

    private static final String IDEMPOTENCY_KEY = "Ibanity-Idempotency-Key";

    public IbanityHttpAdapterListener(final String customerAccessToken, final UUID idempotencyKey) {
        this.customerAccessToken = customerAccessToken;
        this.idempotencyKey = this.idempotencyKey;
    }

    @Override
    public void onBuild(final HttpClientBuilder httpClientBuilder) {
        httpClientBuilder.setSSLContext(IbanityHttpUtils.getSSLContext());
        httpClientBuilder.addInterceptorLast(new IbanitySignatureInterceptor());
        httpClientBuilder.setDefaultHeaders(getAuthorizationtHttpRequestHeaders());
    }

    private Collection<Header> getAuthorizationtHttpRequestHeaders() {
        Collection<Header> authorizationHttpRequestHeaders = new ArrayList();
        if (customerAccessToken != null) {
            authorizationHttpRequestHeaders.add(new BasicHeader(HttpHeaders.AUTHORIZATION, "Bearer " + customerAccessToken));
        }
        if (idempotencyKey != null) {
            authorizationHttpRequestHeaders.add(new BasicHeader(IDEMPOTENCY_KEY, idempotencyKey.toString()));
        }
        return authorizationHttpRequestHeaders;
    }
}
