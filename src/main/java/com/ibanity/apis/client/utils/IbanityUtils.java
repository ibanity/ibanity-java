package com.ibanity.apis.client.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ibanity.apis.client.builders.IbanityConfiguration;
import com.ibanity.apis.client.http.interceptor.IbanitySignatureInterceptor;
import com.ibanity.apis.client.http.interceptor.IdempotencyInterceptor;
import com.ibanity.apis.client.http.service.impl.IbanityHttpSignatureServiceImpl;
import com.ibanity.apis.client.models.SignatureCredentials;
import com.ibanity.apis.client.models.TlsCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.DefaultClientConnectionReuseStrategy;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;

public final class IbanityUtils {

    public static final int DEFAULT_REQUEST_TIMEOUT = 30_000;
    public static final int DEFAULT_JWKS_CACHE_TTL = 60_000;
    public static final int DEFAULT_JWT_CLOCK_SKEW = 30;

    private static final int RETRY_COUNTS = 10;

    private static final String ALIAS_KEY_STORE = "application certificate";
    private static final String CA_TRUST_STORE_KEY = "ibanity-ca";
    private static final String TLS_PROTOCOL = "TLS";

    private IbanityUtils() {

    }

    /**
     * @deprecated  Replaced by {@link #httpClient(IbanityConfiguration)}
     */
    @Deprecated
    public static HttpClient httpClient(Certificate caCertificate,
                                        TlsCredentials tlsCredentials,
                                        SignatureCredentials signatureCertificate,
                                        String basePath) {
        try {
            SSLContext sslContext = getSSLContext(caCertificate, tlsCredentials);
            HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
            IbanityConfiguration ibanityConfiguration = IbanityConfiguration.builder()
                    .signatureCredentials(signatureCertificate)
                    .apiEndpoint(basePath)
                    .build();
            configureHttpClient(sslContext, httpClientBuilder, ibanityConfiguration);
            return httpClientBuilder.build();
        } catch (Exception exception) {
            throw new IllegalArgumentException("An exception occurred while creating IbanityHttpClient", exception);
        }
    }

    public static HttpClient httpClient(IbanityConfiguration configuration) {
        try {
            SSLContext sslContext = getSSLContext(configuration.getCaCertificate(), configuration.getTlsCredentials());
            HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
            configureHttpClient(sslContext, httpClientBuilder, configuration);
            configuration.getHttpRequestInterceptors().forEach(httpClientBuilder::addInterceptorLast);
            configuration.getHttpResponseInterceptors().forEach(httpClientBuilder::addInterceptorFirst);
            return httpClientBuilder.build();
        } catch (Exception exception) {
            throw new IllegalArgumentException("An exception occurred while creating IbanityHttpClient", exception);
        }
    }

    public static ObjectMapper objectMapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule())
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    private static void configureHttpClient(SSLContext sslContext,
                                            HttpClientBuilder httpClientBuilder,
                                            IbanityConfiguration configuration) {
        httpClientBuilder.setSSLContext(sslContext);
        httpClientBuilder.setSSLSocketFactory(new SSLConnectionSocketFactory(sslContext));
        httpClientBuilder.setRetryHandler(new CustomHttpRequestRetryHandler(RETRY_COUNTS, true));
        httpClientBuilder.addInterceptorLast(new IdempotencyInterceptor());
        SignatureCredentials signatureCredentials = configuration.getSignatureCredentials();
        String apiEndpoint = configuration.getApiEndpoint();
        if (signatureCredentials != null) {
            IbanityHttpSignatureServiceImpl httpSignatureService = getIbanityHttpSignatureService(signatureCredentials, apiEndpoint, configuration.getProxyEndpoint());
            httpClientBuilder.addInterceptorLast(new IbanitySignatureInterceptor(httpSignatureService, apiEndpoint));
        }
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(configuration.getConnectTimeout())
                .setSocketTimeout(configuration.getSocketTimeout())
                .setConnectionRequestTimeout(configuration.getConnectionRequestTimeout())
                .build();
        httpClientBuilder.setDefaultRequestConfig(requestConfig);
        httpClientBuilder.setConnectionReuseStrategy(new DefaultClientConnectionReuseStrategy());
    }


    public static SSLContext getSSLContext(IbanityConfiguration ibanityConfiguration) {
        return getSSLContext(ibanityConfiguration.getCaCertificate(), ibanityConfiguration.getTlsCredentials());
    }

    public static SSLContext getSSLContext(Certificate caCertificate, TlsCredentials tlsCredentials) {
        try {
            KeyManager[] keyManagers = null;
            if (tlsCredentials != null) {
                KeyStore keyStore = createKeyStore(tlsCredentials);
                keyManagers = createKeyManagers(keyStore, tlsCredentials.getPrivateKeyPassphrase());
            }

            TrustManager[] trustManagers = null;
            if (caCertificate != null) {
                KeyStore trustStore = createTrustStore(caCertificate);
                trustManagers = createTrustManagers(trustStore);
            }
            SSLContext sslContext = SSLContext.getInstance(TLS_PROTOCOL);
            sslContext.init(keyManagers, trustManagers, null);
            return sslContext;
        } catch (Exception exception) {
            throw new IllegalArgumentException("An exception occurred while creating IbanityHttpClient", exception);
        }

    }

    private static TrustManager[] createTrustManagers(KeyStore trustStore) throws KeyStoreException, NoSuchAlgorithmException {
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);
        return trustManagerFactory.getTrustManagers();
    }

    private static KeyStore createTrustStore(Certificate certificate) throws GeneralSecurityException, IOException {
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null);

        if (!trustStore.containsAlias(CA_TRUST_STORE_KEY)) {
            trustStore.setCertificateEntry(CA_TRUST_STORE_KEY, certificate);
        }

        return trustStore;
    }

    private static KeyStore createKeyStore(TlsCredentials tlsCredentials) throws IOException, GeneralSecurityException {
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null);

        if (!keyStore.containsAlias(ALIAS_KEY_STORE)) {
            keyStore.setKeyEntry(
                    ALIAS_KEY_STORE,
                    tlsCredentials.getPrivateKey(),
                    tlsCredentials.getPrivateKeyPassphrase().toCharArray(),
                    new Certificate[]{tlsCredentials.getCertificate()});
        }

        return keyStore;
    }

    private static IbanityHttpSignatureServiceImpl getIbanityHttpSignatureService(SignatureCredentials signatureCertificate, String ibanityApiEndpoint, String proxyEndpoint) {
        return new IbanityHttpSignatureServiceImpl(
                signatureCertificate.getPrivateKey(),
                signatureCertificate.getCertificateId(),
                ibanityApiEndpoint,
                proxyEndpoint);
    }

    private static KeyManager[] createKeyManagers(KeyStore keyStore, String passphrase) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, passphrase.toCharArray());
        return kmf.getKeyManagers();
    }
}
