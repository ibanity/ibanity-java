package com.ibanity.apis.client.network.http.client;

import com.ibanity.apis.client.models.CustomerAccessToken;
import io.crnk.client.http.apache.HttpClientAdapterListener;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;

import java.util.ArrayList;
import java.util.Collection;

public class IbanityAccessTokenAdapterListener implements HttpClientAdapterListener {

    CustomerAccessToken customerAccessToken = null;
    public IbanityAccessTokenAdapterListener() {
    }

    public IbanityAccessTokenAdapterListener(CustomerAccessToken customerAccessToken) {
        this.customerAccessToken = customerAccessToken;
    }

    @Override
    public void onBuild(HttpClientBuilder httpClientBuilder) {
        httpClientBuilder.setSSLContext(IbanityHttpUtils.getSSLContext());
        httpClientBuilder.addInterceptorLast(new IbanitySignatureInterceptor());
        if (customerAccessToken != null){
            httpClientBuilder.setDefaultHeaders(getAuthorizationtHttpRequestHeaders(customerAccessToken));
        }
    }

    private Collection<Header> getAuthorizationtHttpRequestHeaders(CustomerAccessToken customerAccessToken){
        Collection<Header> authorizationHttpRequestHeaders = new ArrayList();
        authorizationHttpRequestHeaders.add(new BasicHeader(HttpHeaders.AUTHORIZATION, "Bearer "+customerAccessToken.getToken()));
        return authorizationHttpRequestHeaders;
    }
}
