package com.ibanity.apis.client.builders;

import com.ibanity.apis.client.services.IbanityService;

import java.security.PrivateKey;
import java.security.cert.Certificate;

public interface OptionalPropertiesBuilder {

    IbanityService build();

    OptionalPropertiesBuilder caCertificate(Certificate certificate);

    RequestSignaturePassphraseBuilder requestSignaturePrivateKey(PrivateKey privateKey);
}
