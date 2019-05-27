package com.ibanity.apis.client.holders;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

@Getter
@ToString
@EqualsAndHashCode
public class CertificateHolder {

    private final X509Certificate publicKey;
    private final PrivateKey privateKey;
    private final String privateKeyPassphrase;

    public CertificateHolder(X509Certificate publicKey, PrivateKey privateKey, String privateKeyPassphrase) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.privateKeyPassphrase = privateKeyPassphrase;
    }
}
