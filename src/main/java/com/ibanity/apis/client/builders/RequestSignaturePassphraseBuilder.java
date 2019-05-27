package com.ibanity.apis.client.builders;

public interface RequestSignaturePassphraseBuilder {

    RequestSignatureCertificateBuilder requestSignaturePassphrase(String passphrase);

    RequestSignatureCertificateBuilder requestSignatureNoPassphrase();
}
