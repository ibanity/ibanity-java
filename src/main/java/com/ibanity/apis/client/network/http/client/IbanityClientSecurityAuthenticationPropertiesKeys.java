package com.ibanity.apis.client.network.http.client;

public class IbanityClientSecurityAuthenticationPropertiesKeys extends IbanityClientSecurityPropertiesKeys {
    private static final String IBANITY_CLIENT_SSL_PRIVATE_CERTIFICATE_PATH_PROPERTY_KEY     = "client.ssl.authentication.certificate.path";
    private static final String IBANITY_CLIENT_SSL_PRIVATE_PRIVATE_KEY_PATH_PROPERTY_KEY      = "client.ssl.authentication.private-key.path";
    private static final String IBANITY_CLIENT_SSL_PRIVATE_PRIVATE_KEY_PASSPHRASE_PROPERTY_KEY = "client.ssl.authentication.private-key.passphrase";

    @Override
    public String getIbanityClientSslCertificatePathPropertyKey() {
        return IBANITY_CLIENT_SSL_PRIVATE_CERTIFICATE_PATH_PROPERTY_KEY;
    }

    @Override
    public String getIbanityClientSslPrivateKeyPathPropertyKey() {
        return IBANITY_CLIENT_SSL_PRIVATE_PRIVATE_KEY_PATH_PROPERTY_KEY;
    }

    @Override
    public String getIbanityClientSslPrivateKeyPassphrasePropertyKey() {
        return IBANITY_CLIENT_SSL_PRIVATE_PRIVATE_KEY_PASSPHRASE_PROPERTY_KEY;
    }
}
