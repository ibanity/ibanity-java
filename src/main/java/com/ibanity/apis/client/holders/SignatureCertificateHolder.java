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

    public SignatureCertificateHolder(X509Certificate signaturePublicKey,
                                      PrivateKey signaturePrivateKey,
                                      String signaturePrivateKeyPassphrase,
                                      String certificateId) {
        super(signaturePublicKey, signaturePrivateKey, signaturePrivateKeyPassphrase);
        this.certificateId = certificateId;
    }
}
