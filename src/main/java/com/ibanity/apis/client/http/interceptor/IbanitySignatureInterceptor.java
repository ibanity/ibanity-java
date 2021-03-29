package com.ibanity.apis.client.http.interceptor;

import com.ibanity.apis.client.http.service.IbanityHttpSignatureService;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ibanity.apis.client.http.IbanityHttpClient.DEFAULT_ENCODING;

public class IbanitySignatureInterceptor implements HttpRequestInterceptor {

    private static final Charset DEFAULT_CHARSET = Charset.forName(DEFAULT_ENCODING);
    private final IbanityHttpSignatureService httpSignatureService;
    private final String basePath;

    public IbanitySignatureInterceptor(IbanityHttpSignatureService httpSignatureService, String basePath) {
        this.httpSignatureService = httpSignatureService;
        this.basePath = basePath;
    }

    @Override
    public void process(final HttpRequest httpRequest, final HttpContext httpContext) throws IOException {
        try {
            HttpRequestWrapper requestWrapper = (HttpRequestWrapper) httpRequest;
            InputStream payload;
            if (requestWrapper.getOriginal() instanceof HttpEntityEnclosingRequestBase) {
                HttpEntityEnclosingRequestBase original = (HttpEntityEnclosingRequestBase) requestWrapper.getOriginal();
                payload = original.getEntity().getContent();
            } else {
                payload = IOUtils.toInputStream("", DEFAULT_CHARSET);
            }

            httpSignatureService.getHttpSignatureHeaders(
                    requestWrapper.getMethod(),
                    getUrl(requestWrapper),
                    getAllHeaders(requestWrapper),
                    payload)
                    .entrySet()
                    .forEach(addHeaderToRequest(httpRequest));
        } catch (Exception exception) {
            throw new IOException(exception.getMessage(), exception);
        }
    }

    private URL getUrl(HttpRequestWrapper requestWrapper) throws MalformedURLException {
        return new URL(basePath + requestWrapper.getURI().toString());
    }

    private Consumer<? super Map.Entry<String, String>> addHeaderToRequest(HttpRequest httpRequest) {
        return (entry) -> httpRequest.addHeader(entry.getKey(), entry.getValue());
    }

    private Map<String, String> getAllHeaders(HttpRequestWrapper requestWrapper) {
        return Stream.of(requestWrapper.getAllHeaders())
                .collect(Collectors.toMap(Header::getName,
                        Header::getValue));
    }

}
