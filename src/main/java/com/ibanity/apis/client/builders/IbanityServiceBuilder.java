package com.ibanity.apis.client.builders;

import com.ibanity.apis.client.models.SignatureCredentials;
import com.ibanity.apis.client.models.TlsCredentials;
import com.ibanity.apis.client.services.IbanityService;
import com.ibanity.apis.client.services.impl.IbanityServiceImpl;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;

import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class IbanityServiceBuilder implements
        OptionalPropertiesBuilder,
        IbanityApiEndpointBuilder,
        TlsPrivateKeyBuilder,
        TlsPassphraseBuilder,
        TlsCertificateBuilder,
        RequestSignaturePrivateKeyBuilder,
        RequestSignaturePassphraseBuilder,
        RequestSignatureCertificateBuilder,
        RequestSignatureCertificateIdBuilder {

    private String apiEndpoint;
    private String proxyEndpoint;

    private Certificate caCertificate;

    private X509Certificate tlsCertificate;
    private PrivateKey tlsPrivateKey;
    private String tlsPrivateKeyPassphrase;

    private X509Certificate signatureCertificate;
    private PrivateKey signaturePrivateKey;
    private String signaturePrivateKeyPassphrase;
    private String signatureCertificateId;
    private String pontoConnectOauth2ClientId;
    private boolean disableTlsClientCertificate;
    private List<HttpRequestInterceptor> requestInterceptors = newArrayList();
    private List<HttpResponseInterceptor> responseInterceptors = newArrayList();

    public static IbanityApiEndpointBuilder builder() {
        return new IbanityServiceBuilder();
    }

    public IbanityService build() {
        SignatureCredentials signatureCredentials = null;
        if (signaturePrivateKey != null) {
            signatureCredentials = SignatureCredentials.builder()
                    .certificate(signatureCertificate)
                    .certificateId(signatureCertificateId)
                    .privateKey(signaturePrivateKey)
                    .privateKeyPassphrase(signaturePrivateKeyPassphrase)
                    .build();
            signaturePrivateKeyPassphrase = null;
        }

        TlsCredentials tlsCredentials = null;
        if(!disableTlsClientCertificate) {
            tlsCredentials = TlsCredentials.builder()
                    .certificate(tlsCertificate)
                    .privateKey(tlsPrivateKey)
                    .privateKeyPassphrase(tlsPrivateKeyPassphrase)
                    .build();
            tlsPrivateKeyPassphrase = null;
        }

        IbanityConfiguration ibanityConfiguration = IbanityConfiguration.builder()
                .apiEndpoint(apiEndpoint)
                .caCertificate(caCertificate)
                .tlsCredentials(tlsCredentials)
                .signatureCredentials(signatureCredentials)
                .proxyEndpoint(proxyEndpoint)
                .pontoConnectOauth2ClientId(pontoConnectOauth2ClientId)
                .httpRequestInterceptors(requestInterceptors)
                .httpResponseInterceptors(responseInterceptors)
                .build();

        return new IbanityServiceImpl(ibanityConfiguration);
    }

    public OptionalPropertiesBuilder caCertificate(Certificate certificate) {
        this.caCertificate = certificate;
        return this;
    }

    public TlsPassphraseBuilder tlsPrivateKey(PrivateKey privateKey) {
        this.tlsPrivateKey = privateKey;
        return this;
    }

    @Override
    public OptionalPropertiesBuilder disableTlsClientCertificate() {
        this.disableTlsClientCertificate = true;
        return this;
    }

    public RequestSignaturePassphraseBuilder requestSignaturePrivateKey(PrivateKey privateKey) {
        this.signaturePrivateKey = privateKey;
        return this;
    }

    @Override
    public OptionalPropertiesBuilder pontoConnectOauth2ClientId(String clientId) {
        this.pontoConnectOauth2ClientId = clientId;
        return this;
    }

    @Override
    public OptionalPropertiesBuilder proxyEndpoint(String proxyEndpoint) {
        this.proxyEndpoint = removeTrailingSlash(proxyEndpoint);;
        return this;
    }

    @Override
    public OptionalPropertiesBuilder withHttpRequestInterceptors(HttpRequestInterceptor... httpRequestInterceptor) {
        this.requestInterceptors.addAll(asList(httpRequestInterceptor));
        return this;
    }

    @Override
    public OptionalPropertiesBuilder withHttpResponseInterceptors(HttpResponseInterceptor... httpResponseInterceptor) {
        this.responseInterceptors.addAll(asList(httpResponseInterceptor));
        return this;
    }

    @Override
    public OptionalPropertiesBuilder tlsCertificate(X509Certificate certificate) {
        this.tlsCertificate = certificate;
        return this;
    }

    @Override
    public IbanityServiceBuilder requestSignatureCertificate(X509Certificate certificate) {
        this.signatureCertificate = certificate;
        return this;
    }

    @Override
    public IbanityServiceBuilder signatureCertificateId(String certificateId) {
        this.signatureCertificateId = certificateId;
        return this;
    }

    @Override
    public TlsCertificateBuilder passphrase(String passphrase) {
        if (isBlank(passphrase)) {
            this.tlsPrivateKeyPassphrase = "";
        } else {
            this.tlsPrivateKeyPassphrase = passphrase;
        }
        return this;
    }

    @Override
    public TlsCertificateBuilder noPassphrase() {
        this.tlsPrivateKeyPassphrase = "";
        return this;
    }

    @Override
    public RequestSignatureCertificateBuilder requestSignaturePassphrase(String passphrase) {
        if (isBlank(passphrase)) {
            this.signaturePrivateKeyPassphrase = "";
        } else {
            this.signaturePrivateKeyPassphrase = passphrase;
        }

        return this;
    }

    @Override
    public RequestSignatureCertificateBuilder requestSignatureNoPassphrase() {
        this.signaturePrivateKeyPassphrase = "";
        return this;
    }

    @Override
    public TlsPrivateKeyBuilder ibanityApiEndpoint(String apiEndpoint) {
        this.apiEndpoint = removeTrailingSlash(apiEndpoint);
        return this;
    }

    private String removeTrailingSlash(String url) {
        return url != null ? url.replaceAll("/\\z", "") : null;
    }

}
