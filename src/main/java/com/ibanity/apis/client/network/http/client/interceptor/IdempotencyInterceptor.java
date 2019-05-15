package com.ibanity.apis.client.network.http.client.interceptor;

import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;

import java.util.UUID;

public class IdempotencyInterceptor implements HttpRequestInterceptor {

    private static final String IDEMPOTENCY_HTTP_HEADER_KEY = "Ibanity-Idempotency-Key";

    @Override
    public void process(final HttpRequest httpRequest, final HttpContext httpContext) {
        if (!httpRequest.getRequestLine().getMethod().equalsIgnoreCase("post")
                && !httpRequest.getRequestLine().getMethod().equalsIgnoreCase("patch")
        ) {
            return;
        }
        httpRequest.addHeader(IDEMPOTENCY_HTTP_HEADER_KEY, UUID.randomUUID().toString());
    }
}
