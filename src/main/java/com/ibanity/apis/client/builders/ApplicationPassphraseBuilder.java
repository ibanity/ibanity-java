package com.ibanity.apis.client.builders;

public interface ApplicationPassphraseBuilder {

    ApplicationCertificateBuilder passphrase(String passphrase);

    ApplicationCertificateBuilder noPassphrase();
}
