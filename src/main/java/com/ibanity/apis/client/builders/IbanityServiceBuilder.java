package com.ibanity.apis.client.builders;

import com.ibanity.apis.client.helpers.IbanityService;
import com.ibanity.apis.client.holders.ApplicationCredentials;
import com.ibanity.apis.client.holders.SignatureCredentials;

import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class IbanityServiceBuilder implements
        OptionalPropertiesBuilder,
        IbanityApiEndpointBuilder,
        ApplicationPrivateKeyBuilder,
        ApplicationPassphraseBuilder,
        ApplicationCertificateBuilder,
        RequestSignaturePrivateKeyBuilder,
        RequestSignaturePassphraseBuilder,
        RequestSignatureCertificateBuilder,
        RequestSignatureCertificateIdBuilder {

    private String apiEndpoint;

    private Certificate caCertificate;

    private X509Certificate tlsCertificate;
    private PrivateKey tlsPrivateKey;
    private String tlsPrivateKeyPassphrase;

    private X509Certificate signatureCertificate;
    private PrivateKey signaturePrivateKey;
    private String signaturePrivateKeyPassphrase;
    private String signatureCertificateId;

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

        ApplicationCredentials applicationCertificate = ApplicationCredentials.builder()
                .certificate(tlsCertificate)
                .privateKey(tlsPrivateKey)
                .privateKeyPassphrase(tlsPrivateKeyPassphrase)
                .build();
        tlsPrivateKeyPassphrase = null;

        return new IbanityService(apiEndpoint, caCertificate, applicationCertificate, signatureCredentials);
    }

    public OptionalPropertiesBuilder caCertificate(Certificate certificate) {
        this.caCertificate = certificate;
        return this;
    }

    public ApplicationPassphraseBuilder applicationPrivateKey(PrivateKey privateKey) {
        this.tlsPrivateKey = privateKey;
        return this;
    }

    public RequestSignaturePassphraseBuilder requestSignaturePrivateKey(PrivateKey privateKey) {
        this.signaturePrivateKey = privateKey;
        return this;
    }

    @Override
    public OptionalPropertiesBuilder applicationCertificate(X509Certificate certificate) {
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
    public ApplicationCertificateBuilder passphrase(String passphrase) {
        if (isBlank(passphrase)) {
            this.tlsPrivateKeyPassphrase = "";
        } else {
            this.tlsPrivateKeyPassphrase = passphrase;
        }
        return this;
    }

    @Override
    public ApplicationCertificateBuilder noPassphrase() {
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
    public ApplicationPrivateKeyBuilder ibanityApiEndpoint(String apiEndpoint) {
        this.apiEndpoint = apiEndpoint;
        return this;
    }
}
