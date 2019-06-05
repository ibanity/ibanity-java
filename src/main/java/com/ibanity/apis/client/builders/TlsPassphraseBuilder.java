package com.ibanity.apis.client.builders;

public interface TlsPassphraseBuilder {

    TlsCertificateBuilder passphrase(String passphrase);

    TlsCertificateBuilder noPassphrase();
}
