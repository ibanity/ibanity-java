package com.ibanity.apis.client.network.http.client;

import com.ibanity.apis.client.services.impl.IbanityHttpSignatureServiceImpl;
import com.ibanity.apis.client.utils.KeyToolHelper;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ibanity.apis.client.configuration.IbanityConfiguration.IBANITY_API_ENDPOINT_PROPERTY_KEY;
import static com.ibanity.apis.client.configuration.IbanityConfiguration.getConfiguration;
import static com.ibanity.apis.client.network.http.client.IbanityClientSecuritySignaturePropertiesKeys.IBANITY_CLIENT_SIGNATURE_CERTIFICATE_ID_PROPERTY_KEY;
import static com.ibanity.apis.client.network.http.client.IbanityClientSecuritySignaturePropertiesKeys.IBANITY_CLIENT_SIGNATURE_CERTIFICATE_PATH_PROPERTY_KEY;
import static com.ibanity.apis.client.network.http.client.IbanityClientSecuritySignaturePropertiesKeys.IBANITY_CLIENT_SIGNATURE_PRIVATE_KEY_PASSPHRASE_PROPERTY_KEY;
import static com.ibanity.apis.client.network.http.client.IbanityClientSecuritySignaturePropertiesKeys.IBANITY_CLIENT_SIGNATURE_PRIVATE_KEY_PATH_PROPERTY_KEY;

public class IbanitySignatureInterceptor implements HttpRequestInterceptor {

    private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
    private final IbanityHttpSignatureServiceImpl httpSignatureService;

    IbanitySignatureInterceptor() {
        httpSignatureService = new IbanityHttpSignatureServiceImpl(getPrivateKey(), (X509Certificate) getCertificate(), getConfiguration(IBANITY_CLIENT_SIGNATURE_CERTIFICATE_ID_PROPERTY_KEY));
    }

    @Override
    public void process(final HttpRequest httpRequest, final HttpContext httpContext) throws IOException {
        try {
            HttpRequestWrapper requestWrapper = (HttpRequestWrapper) httpRequest;
            String payload = "";
            if (requestWrapper.getOriginal() instanceof HttpEntityEnclosingRequestBase) {
                payload = IOUtils.toString(
                        ((HttpEntityEnclosingRequestBase) requestWrapper.getOriginal())
                                .getEntity().getContent(), UTF8_CHARSET);
            }

            httpSignatureService.getHttpSignatureHeaders(
                    requestWrapper.getMethod(),
                    getUrl(requestWrapper),
                    getAllHeaders(requestWrapper),
                    payload)
                    .entrySet()
                    .forEach(addHeaderToRequest(httpRequest));
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    private URL getUrl(HttpRequestWrapper requestWrapper) throws MalformedURLException {
        String host = getConfiguration(IBANITY_API_ENDPOINT_PROPERTY_KEY);
        return new URL(host + requestWrapper.getURI().toString());
    }

    private Consumer<? super Map.Entry<String, String>> addHeaderToRequest(HttpRequest httpRequest) {
        return (entry) -> httpRequest.addHeader(entry.getKey(), entry.getValue());
    }

    private Map<String, String> getAllHeaders(HttpRequestWrapper requestWrapper) {
        return Stream.of(requestWrapper.getAllHeaders())
                .collect(Collectors.toMap(Header::getName,
                        Header::getValue));
    }

    private Certificate getCertificate() {
        try {
            String certificatePath = getConfiguration(IBANITY_CLIENT_SIGNATURE_CERTIFICATE_PATH_PROPERTY_KEY);
            return KeyToolHelper.loadCertificate(certificatePath);
        } catch (CertificateException e) {
            throw new IllegalArgumentException("Invalid certificate configuration", e);
        }
    }

    private PrivateKey getPrivateKey() {
        try {
            String privateKeyPassPhrase = getConfiguration(IBANITY_CLIENT_SIGNATURE_PRIVATE_KEY_PASSPHRASE_PROPERTY_KEY, "");
            return KeyToolHelper.loadPrivateKey(getConfiguration(IBANITY_CLIENT_SIGNATURE_PRIVATE_KEY_PATH_PROPERTY_KEY), privateKeyPassPhrase);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid private key configuration", e);
        }
    }

}
