package com.ibanity.apis.client.http.factory;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.http.impl.IbanityHttpClientImpl;
import com.ibanity.apis.client.models.SignatureCredentials;
import com.ibanity.apis.client.models.TlsCredentials;
import com.ibanity.apis.client.utils.IbanityUtils;
import org.apache.http.client.HttpClient;

import javax.net.ssl.SSLContext;
import java.security.cert.Certificate;

public class IbanityHttpClientFactory {

    /**
     * @deprecated  Replaced by {@link #create(HttpClient, SSLContext)}
     */
    @Deprecated
    public IbanityHttpClient create(Certificate caCertificate,
                                    TlsCredentials tlsCredentials,
                                    SignatureCredentials signatureCertificate,
                                    String basePath) {
        return new IbanityHttpClientImpl(IbanityUtils.httpClient(caCertificate, tlsCredentials, signatureCertificate, basePath), IbanityUtils.getSSLContext(caCertificate, tlsCredentials));
    }

    public IbanityHttpClient create(HttpClient httpClient, SSLContext sslContext) {
        return new IbanityHttpClientImpl(httpClient, sslContext);
    }
}
