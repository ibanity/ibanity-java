package com.ibanity.apis.client.network.http.client;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.util.UUID;

public class IndempotencyInterceptor implements HttpRequestInterceptor {

    private UUID idempotencyKey;

    private static final String IDEMPOTENCY_HTTP_HEADER_KEY = "Ibanity-Idempotency-Key";

    public IndempotencyInterceptor(final UUID idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }

    @Override
    public void process(final HttpRequest httpRequest, final HttpContext httpContext) throws HttpException, IOException {
        if (idempotencyKey != null) {
            httpRequest.addHeader(IDEMPOTENCY_HTTP_HEADER_KEY, idempotencyKey.toString());
        }
    }
}
