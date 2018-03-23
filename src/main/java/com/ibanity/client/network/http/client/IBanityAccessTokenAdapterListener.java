package com.ibanity.client.network.http.client;

import com.ibanity.client.api.configuration.IBanityConfiguration;
import com.ibanity.client.models.CustomerAccessToken;
import io.crnk.client.http.apache.HttpClientAdapterListener;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;

public class IBanityAccessTokenAdapterListener implements HttpClientAdapterListener {
    private static final Logger LOGGER = LogManager.getLogger(IBanityAccessTokenAdapterListener.class);
    private static final String IBANITY_CLIENT_SSL_PRIVATE_CERTIFICATE_STANDARD_PROPERTY_KEY                = IBanityConfiguration.IBANITY_PROPERTIES_PREFIX + "client.ssl.private.certificate.standard";
    private static final String IBANITY_CLIENT_SSL_PRIVATE_CERTIFICATE_PATH_PROPERTY_KEY                    = IBanityConfiguration.IBANITY_PROPERTIES_PREFIX + "client.ssl.private.certificate.path";
    private static final String IBANITY_CLIENT_SSL_PRIVATE_CERTIFICATE_PRIVATE_KEY_PASSOWRD_PROPERTY_KEY    = IBanityConfiguration.IBANITY_PROPERTIES_PREFIX + "client.ssl.private.certificate.private_key.password";
    private static final String IBANITY_CLIENT_SSL_PRIVATE_CERTIFICATE_TRUSTMANAGER_PROPERTY_KEY            = IBanityConfiguration.IBANITY_PROPERTIES_PREFIX + "client.ssl.private.certificate.trustmanager";
    private static final String IBANITY_CLIENT_SSL_PROTOCOL_PROPERTY_KEY                                    = IBanityConfiguration.IBANITY_PROPERTIES_PREFIX + "client.ssl.protocol";

    CustomerAccessToken customerAccessToken = null;
    public IBanityAccessTokenAdapterListener() {
    }

    public IBanityAccessTokenAdapterListener(CustomerAccessToken customerAccessToken) {
        this.customerAccessToken = customerAccessToken;
    }

    @Override
    public void onBuild(HttpClientBuilder httpClientBuilder) {
        httpClientBuilder.setSSLContext(IBanityHttpUtils.getSSLContext());
        httpClientBuilder.addInterceptorLast(new IBanitySignatureInterceptor());
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
