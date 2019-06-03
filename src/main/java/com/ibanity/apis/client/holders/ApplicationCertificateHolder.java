package com.ibanity.apis.client.holders;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ApplicationCertificateHolder extends CertificateHolder {

    private final String sslProtocol;

    public ApplicationCertificateHolder(X509Certificate certificate,
                                        PrivateKey privateKey,
                                        String privateKeyPassphrase,
                                        String sslProtocol) {
        super(certificate, privateKey, privateKeyPassphrase);
        this.sslProtocol = sslProtocol;
    }
}
