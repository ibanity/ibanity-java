package com.ibanity.apis.client.builders;

import com.ibanity.apis.client.models.SignatureCredentials;
import com.ibanity.apis.client.models.TlsCredentials;
import com.ibanity.apis.client.services.IbanityService;
import com.ibanity.apis.client.services.impl.IbanityServiceImpl;

import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

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

        TlsCredentials tlsCredentials = TlsCredentials.builder()
                .certificate(tlsCertificate)
                .privateKey(tlsPrivateKey)
                .privateKeyPassphrase(tlsPrivateKeyPassphrase)
                .build();
        tlsPrivateKeyPassphrase = null;

        return new IbanityServiceImpl(apiEndpoint, caCertificate, tlsCredentials, signatureCredentials, pontoConnectOauth2ClientId, proxyEndpoint);
    }

    public OptionalPropertiesBuilder caCertificate(Certificate certificate) {
        this.caCertificate = certificate;
        return this;
    }

    public TlsPassphraseBuilder tlsPrivateKey(PrivateKey privateKey) {
        this.tlsPrivateKey = privateKey;
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
        this.proxyEndpoint = proxyEndpoint;
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
        this.apiEndpoint = apiEndpoint;
        return this;
    }
}
