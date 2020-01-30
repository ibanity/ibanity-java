package com.ibanity.apis.client.http.impl;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static com.ibanity.apis.client.helpers.IbanityTestHelper.createHttpResponse;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IbanityHttpClientImplTest {

    @InjectMocks
    private IbanityHttpClientImpl ibanityHttpClient;

    @Mock
    private HttpClient httpClient;

    @Captor
    private ArgumentCaptor<HttpRequestBase> requestArgumentCaptor;

    @Test
    void get() throws Exception {
        String expected = "value";

        when(httpClient.execute(requestArgumentCaptor.capture())).thenReturn(createHttpResponse(expected));

        HttpResponse actual = ibanityHttpClient.get(uri());

        assertThat(readContent(actual)).isEqualTo(expected);
        assertThat(requestArgumentCaptor.getValue().getMethod()).isEqualTo("GET");
    }

    private URI uri() throws URISyntaxException {
        return new URI("www.google.be");
    }

    @Test
    void post() throws Exception {
        String expected = "value";

        when(httpClient.execute(requestArgumentCaptor.capture())).thenReturn(createHttpResponse(expected));

        HttpResponse actual = ibanityHttpClient.post(uri(), "hello");

        assertThat(readContent(actual)).isEqualTo(expected);
        assertThat(requestArgumentCaptor.getValue().getMethod()).isEqualTo("POST");
    }

    @Test
    void post_whenCustomerToken_thenAddHeader() throws Exception {
        String expected = "value";

        when(httpClient.execute(requestArgumentCaptor.capture())).thenReturn(createHttpResponse(expected));

        HttpResponse actual = ibanityHttpClient.post(uri(), "hello", "accessToken");

        assertThat(readContent(actual)).isEqualTo(expected);
        assertThat(requestArgumentCaptor.getValue().getMethod()).isEqualTo("POST");
        Header authorizationsHeader = requestArgumentCaptor.getValue().getFirstHeader("Authorization");
        assertThat(authorizationsHeader.getValue()).isEqualTo("Bearer accessToken");
    }

    @Test
    void delete() throws Exception {
        String expected = "value";

        when(httpClient.execute(requestArgumentCaptor.capture())).thenReturn(createHttpResponse(expected));

        HttpResponse actual = ibanityHttpClient.delete(uri());

        assertThat(readContent(actual)).isEqualTo(expected);
        assertThat(requestArgumentCaptor.getValue().getMethod()).isEqualTo("DELETE");
    }

    @Test
    void patch() throws Exception {
        String expected = "value";

        when(httpClient.execute(requestArgumentCaptor.capture())).thenReturn(createHttpResponse(expected));

        HttpResponse actual = ibanityHttpClient.patch(uri(), "hello");

        assertThat(readContent(actual)).isEqualTo(expected);
        assertThat(requestArgumentCaptor.getValue().getMethod()).isEqualTo("PATCH");
    }

    private String readContent(HttpResponse actual) throws IOException {
        return IOUtils.toString(actual.getEntity().getContent(), UTF_8);
    }
}
