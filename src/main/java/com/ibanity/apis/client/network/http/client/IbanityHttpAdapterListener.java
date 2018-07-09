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

    public IbanityHttpAdapterListener(final String customerAccessToken, final UUID idempotencyKey) {
        this.customerAccessToken = customerAccessToken;
        this.idempotencyKey = idempotencyKey;
    }

    @Override
    public void onBuild(final HttpClientBuilder httpClientBuilder) {
        httpClientBuilder.setSSLContext(IbanityHttpUtils.getSSLContext(new IbanityClientSecurityAuthenticationPropertiesKeys()));
        httpClientBuilder.setDefaultHeaders(getAuthorizationtHttpRequestHeaders());
        if (idempotencyKey != null) {
            httpClientBuilder.addInterceptorLast(new IdempotencyInterceptor(idempotencyKey));
        }
        if (customerAccessToken != null) {
            httpClientBuilder.addInterceptorLast(new IbanitySignatureInterceptor());
        }
    }

    private Collection<Header> getAuthorizationtHttpRequestHeaders() {
        Collection<Header> authorizationHttpRequestHeaders = new ArrayList();
        if (customerAccessToken != null) {
            authorizationHttpRequestHeaders.add(new BasicHeader(HttpHeaders.AUTHORIZATION, "Bearer " + customerAccessToken));
        }
        return authorizationHttpRequestHeaders;
    }
}
