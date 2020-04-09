package com.ibanity.apis.client.http.factory;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.http.impl.IbanityHttpClientImpl;
import com.ibanity.apis.client.models.SignatureCredentials;
import com.ibanity.apis.client.models.TlsCredentials;
import com.ibanity.apis.client.utils.IbanityUtils;
import org.apache.http.client.HttpClient;

import java.security.cert.Certificate;

public class IbanityHttpClientFactory {

    /**
     * @deprecated  Replaced by {@link #create(HttpClient)}
     */
    @Deprecated
    public IbanityHttpClient create(Certificate caCertificate,
                                    TlsCredentials tlsCredentials,
                                    SignatureCredentials signatureCertificate,
                                    String basePath) {
        return new IbanityHttpClientImpl(IbanityUtils.httpClient(caCertificate, tlsCredentials, signatureCertificate, basePath));
    }

    public IbanityHttpClient create(HttpClient httpClient) {
        return new IbanityHttpClientImpl(httpClient);
    }
}
