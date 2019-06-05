package com.ibanity.apis.client.http.impl;

import com.ibanity.apis.client.http.handler.IbanityResponseHandler;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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

        when(httpClient.execute(requestArgumentCaptor.capture(), any(IbanityResponseHandler.class))).thenReturn(expected);

        String actual = ibanityHttpClient.get(uri());

        assertThat(actual).isEqualTo(expected);
        assertThat(requestArgumentCaptor.getValue().getMethod()).isEqualTo("GET");
    }

    private URI uri() throws URISyntaxException {
        return new URI("www.google.be");
    }

    @Test
    void post() throws Exception {
        String expected = "value";

        when(httpClient.execute(requestArgumentCaptor.capture(), any(IbanityResponseHandler.class))).thenReturn(expected);

        String actual = ibanityHttpClient.post(uri(), "hello");

        assertThat(actual).isEqualTo(expected);
        assertThat(requestArgumentCaptor.getValue().getMethod()).isEqualTo("POST");
    }

    @Test
    void post_whenCustomerToken_thenAddHeader() throws Exception {
        String expected = "value";

        when(httpClient.execute(requestArgumentCaptor.capture(), any(IbanityResponseHandler.class))).thenReturn(expected);

        String actual = ibanityHttpClient.post(uri(), "hello", "accessToken");

        assertThat(actual).isEqualTo(expected);
        assertThat(requestArgumentCaptor.getValue().getMethod()).isEqualTo("POST");
        Header authorizationsHeader = requestArgumentCaptor.getValue().getFirstHeader("Authorization");
        assertThat(authorizationsHeader.getValue()).isEqualTo("Bearer accessToken");
    }

    @Test
    void delete() throws Exception {
        String expected = "value";

        when(httpClient.execute(requestArgumentCaptor.capture(), any(IbanityResponseHandler.class))).thenReturn(expected);

        String actual = ibanityHttpClient.delete(uri());

        assertThat(actual).isEqualTo(expected);
        assertThat(requestArgumentCaptor.getValue().getMethod()).isEqualTo("DELETE");
    }
}