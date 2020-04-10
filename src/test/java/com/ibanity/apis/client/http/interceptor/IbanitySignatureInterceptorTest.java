package com.ibanity.apis.client.http.interceptor;

import com.ibanity.apis.client.http.service.IbanityHttpSignatureService;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HttpContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IbanitySignatureInterceptorTest {

    private static final String BASE_PATH = "https://api.ibanity.localhost";

    private IbanitySignatureInterceptor ibanitySignatureInterceptor;

    @Mock
    private IbanityHttpSignatureService ibanityHttpSignatureService;

    @Mock
    private HttpRequestWrapper httpRequestWrapper;

    @Mock
    private HttpEntityEnclosingRequestBase httpRequest;

    @Mock
    private HttpContext httpContext;

    @BeforeEach
    void setUp() {
        ibanitySignatureInterceptor = new IbanitySignatureInterceptor(ibanityHttpSignatureService, BASE_PATH);
    }

    @Test
    void process() throws IOException, URISyntaxException {
        String httpMethod = "POST";
        String body = "aBody";

        when(httpRequestWrapper.getURI()).thenReturn(new URI("/path"));
        when(httpRequestWrapper.getAllHeaders()).thenReturn(new BasicHeader[0]);
        when(httpRequestWrapper.getMethod()).thenReturn(httpMethod);
        when(httpRequestWrapper.getOriginal()).thenReturn(httpRequest);
        when(httpRequest.getEntity()).thenReturn(EntityBuilder.create().setText(body).build());

        HashMap<String, String> headers = getSignatureHeaders();
        when(ibanityHttpSignatureService.getHttpSignatureHeaders(httpMethod, getUrl(), getRequestedHeaders(), body))
                .thenReturn(headers);

        ibanitySignatureInterceptor.process(httpRequestWrapper, httpContext);

        verify(httpRequestWrapper).addHeader("digest", "value");
    }

    private HashMap<String, String> getSignatureHeaders() {
        HashMap<String, String> headers = newHashMap();
        headers.put("digest", "value");
        return headers;
    }

    private URL getUrl() throws MalformedURLException {
        return new URL(BASE_PATH + "/path");
    }

    private Map<String, String> getRequestedHeaders() {
        return newHashMap();
    }
}
