package com.ibanity.apis.client.builders;

import java.security.PrivateKey;

public interface TlsPrivateKeyBuilder {

    TlsPassphraseBuilder tlsPrivateKey(PrivateKey privateKey);

    OptionalPropertiesBuilder disableTlsClientCertificate();
}
