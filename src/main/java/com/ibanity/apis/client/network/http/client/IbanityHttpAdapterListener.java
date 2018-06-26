package com.ibanity.apis.client.network.http.client;

import com.ibanity.apis.client.models.CustomerAccessToken;
import io.crnk.client.http.apache.HttpClientAdapterListener;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class IbanityHttpAdapterListener implements HttpClientAdapterListener {

    private CustomerAccessToken customerAccessToken = null;
    private UUID idempotency                        = null;

    private static final String IDEMPOTENCY_KEY = "Ibanity-Idempotency-Key";

    public IbanityHttpAdapterListener(final CustomerAccessToken customerAccessToken, final UUID idempotency) {
        this.customerAccessToken = customerAccessToken;
        this.idempotency = idempotency;
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
            authorizationHttpRequestHeaders.add(new BasicHeader(HttpHeaders.AUTHORIZATION, "Bearer " + customerAccessToken.getToken()));
        }
        if (idempotency != null) {
            authorizationHttpRequestHeaders.add(new BasicHeader(IDEMPOTENCY_KEY, idempotency.toString()));
        }
        return authorizationHttpRequestHeaders;
    }
}
