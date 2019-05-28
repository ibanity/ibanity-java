package com.ibanity.apis.client.builders;

import com.ibanity.apis.client.helpers.IbanityService;

import java.security.PrivateKey;
import java.security.cert.Certificate;

public interface OptionalPropertiesBuilder {

    IbanityService build();

    OptionalPropertiesBuilder caCertificate(Certificate certificate);

    OptionalPropertiesBuilder sslProtocol(String sslProtocol);

    RequestSignaturePassphraseBuilder requestSignaturePrivateKey(PrivateKey privateKey);
}
