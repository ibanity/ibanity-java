package com.ibanity.apis.client.holders;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class SignatureCredentials {

    private X509Certificate certificate;
    private PrivateKey privateKey;
    private String privateKeyPassphrase;
    private String certificateId;
}
