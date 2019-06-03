package com.ibanity.apis.client.holders;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SignatureCertificateHolder extends CertificateHolder {

    private final String certificateId;

    public SignatureCertificateHolder(X509Certificate certificate,
                                      PrivateKey privateKey,
                                      String privateKeyPassphrase,
                                      String certificateId) {
        super(certificate, privateKey, privateKeyPassphrase);
        this.certificateId = certificateId;
    }
}
