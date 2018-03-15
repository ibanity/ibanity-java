package com.ibanity.network.http.client;

import com.ibanity.api.configuration.IbanityConfiguration;
import com.ibanity.models.CustomerAccessToken;
import io.crnk.client.http.apache.HttpClientAdapterListener;
import org.apache.commons.configuration2.Configuration;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Collection;

public class HttpClientIbanityIntegration implements HttpClientAdapterListener {
    private static final Logger LOGGER = LogManager.getLogger(HttpClientIbanityIntegration.class);
    CustomerAccessToken customerAccessToken = null;
    public HttpClientIbanityIntegration() {
    }

    public HttpClientIbanityIntegration(CustomerAccessToken customerAccessToken) {
        this.customerAccessToken = customerAccessToken;
    }

    @Override
    public void onBuild(HttpClientBuilder httpClientBuilder) {
        httpClientBuilder.setSSLContext(getSSLContext());
        if (customerAccessToken != null){
            httpClientBuilder.setDefaultHeaders(getAuthorizationtHttpRequestHeaders(customerAccessToken));
        }
    }

//    private HttpResponseInterceptor getLastHttpResponseInterceptor(){
//        return  new HttpResponseInterceptor() {
//            @Override
//            public void process(org.apache.http.HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
//                HttpEntity entity = httpResponse.getEntity();
//                String responseString = EntityUtils.toString(entity, "UTF-8");
//            }
//        };
//    }

    private Collection<Header> getAuthorizationtHttpRequestHeaders(CustomerAccessToken customerAccessToken){
        Collection<Header> authorizationHttpRequestHeaders = new ArrayList();
        authorizationHttpRequestHeaders.add(new BasicHeader(HttpHeaders.AUTHORIZATION, "Bearer "+customerAccessToken.getToken()));
        return authorizationHttpRequestHeaders;
    }

//    private Collection<Header> getDefaultHttpRequestHeaders(){
//        Collection<Header> defaultHttpRequestHeaders = new ArrayList();
//        defaultHttpRequestHeaders.add(new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json"));
//        defaultHttpRequestHeaders.add(new BasicHeader(HttpHeaders.ACCEPT, "application/json"));
//        return defaultHttpRequestHeaders;
//    }

    private SSLContext getSSLContext() {
        try {
            Configuration ibanityConfiguration = IbanityConfiguration.getConfiguration();
            KeyStore ks = KeyStore.getInstance(ibanityConfiguration.getString("ibanity.client.ssl.private.certificate.standard"));
            FileInputStream fis = new FileInputStream(ibanityConfiguration.getString("ibanity.client.ssl.private.certificate.path"));
            char[] passwordCharArray = ibanityConfiguration.getString("ibanity.client.ssl.private.certificate.password").toCharArray();
            ks.load(fis, passwordCharArray);
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(ibanityConfiguration.getString("ibanity.client.ssl.private.certificate.trustmanager"));
            kmf.init(ks, passwordCharArray);
            SSLContext sc = SSLContext.getInstance(ibanityConfiguration.getString("ibanity.client.ssl.protocol"));
            sc.init(kmf.getKeyManagers(), null, null);
            return sc;
        } catch (Exception e) {
            LOGGER.fatal(e);
            return null;
        }
    }
}
