package com.ibanity.apis.client.builders;

import com.ibanity.apis.client.helpers.IbanityService;
import com.ibanity.apis.client.holders.ApplicationCertificateHolder;
import com.ibanity.apis.client.holders.SignatureCertificateHolder;

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
    private String sslProtocol = "TLS";

    private Certificate caCertificate;

    private X509Certificate publicKey;
    private PrivateKey privateKey;
    private String privateKeyPassphrase;

    private X509Certificate signaturePublicKey;
    private PrivateKey signaturePrivateKey;
    private String signaturePrivateKeyPassphrase;
    private String signatureCertificateId;

    public static IbanityApiEndpointBuilder builder() {
        return new IbanityServiceBuilder();
    }

    public IbanityService build() {
        SignatureCertificateHolder signatureCertificate = null;
        if (signaturePrivateKey != null) {
            signatureCertificate = new SignatureCertificateHolder(signaturePublicKey, signaturePrivateKey, signaturePrivateKeyPassphrase, signatureCertificateId);
        }

        ApplicationCertificateHolder applicationCertificate = new ApplicationCertificateHolder(publicKey, privateKey, privateKeyPassphrase, sslProtocol);
        return new IbanityService(apiEndpoint, caCertificate, applicationCertificate, signatureCertificate);
    }

    public OptionalPropertiesBuilder caCertificate(Certificate certificate) {
        this.caCertificate = certificate;
        return this;
    }

    @Override
    public OptionalPropertiesBuilder sslProtocol(String sslProtocol) {
        this.sslProtocol = sslProtocol;
        return this;
    }

    public ApplicationPassphraseBuilder applicationPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
        return this;
    }

    public RequestSignaturePassphraseBuilder requestSignaturePrivateKey(PrivateKey privateKey) {
        this.signaturePrivateKey = privateKey;
        return this;
    }

    @Override
    public OptionalPropertiesBuilder applicationCertificate(X509Certificate certificate) {
        this.publicKey = certificate;
        return this;
    }

    @Override
    public IbanityServiceBuilder requestSignatureCertificate(X509Certificate certificate) {
        this.signaturePublicKey = certificate;
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
            this.privateKeyPassphrase = "";
        } else {
            this.privateKeyPassphrase = passphrase;
        }
        return this;
    }

    @Override
    public ApplicationCertificateBuilder noPassphrase() {
        this.privateKeyPassphrase = "";
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
