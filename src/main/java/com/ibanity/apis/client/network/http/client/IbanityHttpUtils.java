package com.ibanity.apis.client.network.http.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ibanity.apis.client.holders.ApplicationCertificateHolder;
import com.ibanity.apis.client.holders.CertificateHolder;
import com.ibanity.apis.client.holders.SignatureCertificateHolder;
import com.ibanity.apis.client.network.http.client.interceptor.IbanitySignatureInterceptor;
import com.ibanity.apis.client.network.http.client.interceptor.IdempotencyInterceptor;
import com.ibanity.apis.client.services.impl.IbanityHttpSignatureServiceImpl;
import com.ibanity.apis.client.utils.CustomHttpRequestRetryHandler;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.DefaultClientConnectionReuseStrategy;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;

public final class IbanityHttpUtils {

    private static final int RETRY_COUNTS = 10;
    private static final int DEFAULT_REQUEST_TIMEOUT = 10_000;

    private static final String KEY_ENTRY_NAME = "application certificate";

    private IbanityHttpUtils() {

    }

    public static HttpClient httpClient(Certificate caCertificate,
                                        ApplicationCertificateHolder applicationCertificate,
                                        SignatureCertificateHolder signatureCertificate,
                                        String host) {
        try {
            SSLContext sslContext = getSSLContext(caCertificate, applicationCertificate);
            HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
            configureHttpClient(sslContext, httpClientBuilder, signatureCertificate, host);
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
                                            SignatureCertificateHolder signatureCertificate,
                                            String host) {
        httpClientBuilder.setSSLContext(sslContext);
        httpClientBuilder.setSSLSocketFactory(new SSLConnectionSocketFactory(sslContext));
        httpClientBuilder.setRetryHandler(new CustomHttpRequestRetryHandler(RETRY_COUNTS, true));
        httpClientBuilder.addInterceptorLast(new IdempotencyInterceptor());
        if (signatureCertificate != null) {
            IbanityHttpSignatureServiceImpl httpSignatureService = getIbanityHttpSignatureService(signatureCertificate);
            httpClientBuilder.addInterceptorLast(new IbanitySignatureInterceptor(httpSignatureService, host));
        }
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(DEFAULT_REQUEST_TIMEOUT)
                .setSocketTimeout(DEFAULT_REQUEST_TIMEOUT)
                .setConnectionRequestTimeout(DEFAULT_REQUEST_TIMEOUT)
                .build();
        httpClientBuilder.setDefaultRequestConfig(requestConfig);
        httpClientBuilder.setConnectionReuseStrategy(new DefaultClientConnectionReuseStrategy());
    }

    private static SSLContext getSSLContext(Certificate caCertificate, ApplicationCertificateHolder applicationCertificate) throws IOException, GeneralSecurityException {

        KeyStore keyStore = createKeyStore(applicationCertificate);
        KeyManager[] keyManagers = createKeyManagers(keyStore, applicationCertificate.getPrivateKeyPassphrase());

        KeyStore trustStore = createTrustStore(caCertificate);
        TrustManager[] trustManagers = createTrustManagers(trustStore);

        SSLContext sslContext = SSLContext.getInstance(applicationCertificate.getSslProtocol());
        sslContext.init(keyManagers, trustManagers, null);

        return sslContext;
    }

    private static TrustManager[] createTrustManagers(KeyStore trustStore) throws KeyStoreException, NoSuchAlgorithmException {
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);
        return trustManagerFactory.getTrustManagers();
    }

    private static KeyStore createTrustStore(Certificate certificate) throws GeneralSecurityException, IOException {
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null);

        if (certificate != null && !trustStore.containsAlias("ibanity-ca")) {
            trustStore.setCertificateEntry("ibanity-ca", certificate);
        }

        return trustStore;
    }

    private static KeyStore createKeyStore(CertificateHolder applicationCertificate) throws IOException, GeneralSecurityException {
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null);

        addEntryIfNotPresent(
                keyStore,
                KEY_ENTRY_NAME,
                applicationCertificate.getPrivateKey(),
                applicationCertificate.getPrivateKeyPassphrase(),
                new Certificate[]{applicationCertificate.getPublicKey()});

        return keyStore;
    }

    private static IbanityHttpSignatureServiceImpl getIbanityHttpSignatureService(SignatureCertificateHolder signatureCertificate) {
        return new IbanityHttpSignatureServiceImpl(
                signatureCertificate.getPrivateKey(),
                signatureCertificate.getPublicKey(),
                signatureCertificate.getCertificateId());
    }

    private static KeyManager[] createKeyManagers(KeyStore keyStore, String passphrase) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, passphrase.toCharArray());
        return kmf.getKeyManagers();
    }

    public static void addEntryIfNotPresent(final KeyStore keyStore, final String alias,
                                            final PrivateKey privateKey, String privateKeyPassphrase, final Certificate[] certChain)
            throws GeneralSecurityException {

        if (!keyStore.containsAlias(alias)) {
            keyStore.setKeyEntry(alias, privateKey, privateKeyPassphrase.toCharArray(), certChain);
        }
    }
}