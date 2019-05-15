package com.ibanity.apis.client.network.http.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ibanity.apis.client.utils.CustomHttpRequestRetryHandler;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.DefaultClientConnectionReuseStrategy;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class HttpClientHelper {

    private static final int DEFAULT_REQUEST_TIMEOUT = 10_000;
    private static final int RETRY_COUNTS = 10;

    public HttpClientHelper() {
    }

    static HttpClient httpClient() throws IOException, GeneralSecurityException {
        SSLContext sslContext = IbanityHttpUtils.getSSLContext();
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setSSLContext(sslContext);
        httpClientBuilder.setSSLSocketFactory(new SSLConnectionSocketFactory(sslContext));
        httpClientBuilder.setRetryHandler(new CustomHttpRequestRetryHandler(RETRY_COUNTS, true));
        httpClientBuilder.addInterceptorLast(new IdempotencyInterceptor());
        httpClientBuilder.addInterceptorLast(new IbanitySignatureInterceptor());
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(DEFAULT_REQUEST_TIMEOUT)
                .setSocketTimeout(DEFAULT_REQUEST_TIMEOUT)
                .setConnectionRequestTimeout(DEFAULT_REQUEST_TIMEOUT)
                .build();
        httpClientBuilder.setDefaultRequestConfig(requestConfig);
        httpClientBuilder.setConnectionReuseStrategy(new DefaultClientConnectionReuseStrategy());
        return httpClientBuilder.build();
    }

    public static ObjectMapper objectMapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule())
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
}