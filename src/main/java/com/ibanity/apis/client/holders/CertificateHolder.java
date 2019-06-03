package com.ibanity.apis.client.holders;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class CertificateHolder {

    private final X509Certificate certificate;
    private final PrivateKey privateKey;
    private final String privateKeyPassphrase;
}
