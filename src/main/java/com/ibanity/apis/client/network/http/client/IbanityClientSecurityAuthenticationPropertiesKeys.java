package com.ibanity.apis.client.network.http.client;

public class IbanityClientSecurityAuthenticationPropertiesKeys extends IbanityClientSecurityPropertiesKeys {
    private static final String IBANITY_CLIENT_SSL_CLIENT_CERTIFICATE_PATH_PROPERTY_KEY = "client.ssl.client.certificate.path";
    private static final String IBANITY_CLIENT_SSL_CLIENT_PRIVATE_KEY_PATH_PROPERTY_KEY = "client.ssl.client.private-key.path";
    private static final String IBANITY_CLIENT_SSL_CLIENT_PRIVATE_KEY_PASSPHRASE_PROPERTY_KEY = "client.ssl.client.private-key.passphrase";

    @Override
    public String getIbanityClientCertificatePathPropertyKey() {
        return IBANITY_CLIENT_SSL_CLIENT_CERTIFICATE_PATH_PROPERTY_KEY;
    }

    @Override
    public String getIbanityClientPrivateKeyPathPropertyKey() {
        return IBANITY_CLIENT_SSL_CLIENT_PRIVATE_KEY_PATH_PROPERTY_KEY;
    }

    @Override
    public String getIbanityClientPrivateKeyPassphrasePropertyKey() {
        return IBANITY_CLIENT_SSL_CLIENT_PRIVATE_KEY_PASSPHRASE_PROPERTY_KEY;
    }
}
