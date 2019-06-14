package com.ibanity.apis.client.http.interceptor;

import org.apache.http.HttpRequest;
import org.apache.http.RequestLine;
import org.apache.http.protocol.HttpContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IdempotencyInterceptorTest {

    @Mock
    private HttpRequest httpRequest;

    @Mock
    private HttpContext httpContext;

    @Mock
    private RequestLine requestLine;

    private IdempotencyInterceptor idempotencyInterceptor = new IdempotencyInterceptor();

    @Test
    void process_whenAlreadyAKey_thenDontOverride() {
        when(httpRequest.containsHeader("ibanity-idempotency-key")).thenReturn(true);

        idempotencyInterceptor.process(httpRequest, httpContext);

        verify(httpRequest, never()).addHeader(anyString(), anyString());
    }

    @Test
    void process_whenPost() {
        when(httpRequest.containsHeader("ibanity-idempotency-key")).thenReturn(false);
        when(httpRequest.getRequestLine()).thenReturn(requestLine);
        when(requestLine.getMethod()).thenReturn("POST");

        idempotencyInterceptor.process(httpRequest, httpContext);

        verify(httpRequest).addHeader(eq("ibanity-idempotency-key"), anyString());
    }

    @Test
    void process_whenPatch() {
        when(httpRequest.containsHeader("ibanity-idempotency-key")).thenReturn(false);
        when(httpRequest.getRequestLine()).thenReturn(requestLine);
        when(requestLine.getMethod()).thenReturn("PATCH");

        idempotencyInterceptor.process(httpRequest, httpContext);

        verify(httpRequest).addHeader(eq("ibanity-idempotency-key"), anyString());
    }

    @Test
    void process_whenGet_thenNoIdempotencyKey() {
        when(httpRequest.containsHeader("ibanity-idempotency-key")).thenReturn(false);
        when(httpRequest.getRequestLine()).thenReturn(requestLine);
        when(requestLine.getMethod()).thenReturn("GET");

        idempotencyInterceptor.process(httpRequest, httpContext);

        verify(httpRequest, never()).addHeader(anyString(), anyString());
    }
}