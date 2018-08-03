package com.ibanity.apis.client.network.http.client;

public class IbanityClientSecuritySignaturePropertiesKeys extends IbanityClientSecurityPropertiesKeys {
    private static final String IBANITY_CLIENT_SIGNATURE_CERTIFICATE_PATH_PROPERTY_KEY = "client.signature.certificate.path";
    private static final String IBANITY_CLIENT_SIGNATURE_CERTIFICATE_ID_PROPERTY_KEY = "client.signature.certificate.id";
    private static final String IBANITY_CLIENT_SIGNATURE_PRIVATE_KEY_PATH_PROPERTY_KEY = "client.signature.private-key.path";
    private static final String IBANITY_CLIENT_SIGNATURE_PRIVATE_KEY_PASSPHRASE_PROPERTY_KEY = "client.signature.private-key.passphrase";

    @Override
    public String getIbanityClientCertificatePathPropertyKey() {
        return IBANITY_CLIENT_SIGNATURE_CERTIFICATE_PATH_PROPERTY_KEY;
    }

    @Override
    public String getIbanityClientPrivateKeyPathPropertyKey() {
        return IBANITY_CLIENT_SIGNATURE_PRIVATE_KEY_PATH_PROPERTY_KEY;
    }

    @Override
    public String getIbanityClientPrivateKeyPassphrasePropertyKey() {
        return IBANITY_CLIENT_SIGNATURE_PRIVATE_KEY_PASSPHRASE_PROPERTY_KEY;
    }

    public String getIbanityClientSignatureCertificateIdPropertyKey() {
        return IBANITY_CLIENT_SIGNATURE_CERTIFICATE_ID_PROPERTY_KEY;
    }
}
