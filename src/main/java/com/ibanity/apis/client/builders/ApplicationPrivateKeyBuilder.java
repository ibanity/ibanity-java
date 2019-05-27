package com.ibanity.apis.client.builders;

import java.security.PrivateKey;

public interface ApplicationPrivateKeyBuilder {

    ApplicationPassphraseBuilder applicationPrivateKey(PrivateKey privateKey);
}
