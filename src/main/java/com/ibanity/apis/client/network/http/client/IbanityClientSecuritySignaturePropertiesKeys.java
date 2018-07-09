package com.ibanity.apis.client.network.http.client;

import com.ibanity.apis.client.configuration.IbanityConfiguration;

public class IbanityClientSecuritySignaturePropertiesKeys extends IbanityClientSecurityPropertiesKeys {
    public static final String IBANITY_CLIENT_SSL_PRIVATE_CERTIFICATE_PATH_PROPERTY_KEY = IbanityConfiguration.IBANITY_PROPERTIES_PREFIX + "client.ssl.signature.certificate.path";
    public static final String IBANITY_CLIENT_SSL_PRIVATE_CERTIFICATE_ID_PROPERTY_KEY                   = IbanityConfiguration.IBANITY_PROPERTIES_PREFIX + "client.ssl.signature.certificate.id";
    public static final String IBANITY_CLIENT_SSL_PRIVATE_CERTIFICATE_PRIVATE_KEY_PATH_PROPERTY_KEY     = IbanityConfiguration.IBANITY_PROPERTIES_PREFIX + "client.ssl.signature.certificate.private_key.path";
    public static final String IBANITY_CLIENT_SSL_PRIVATE_CERTIFICATE_PRIVATE_KEY_PASSWORD_PROPERTY_KEY = IbanityConfiguration.IBANITY_PROPERTIES_PREFIX + "client.ssl.signature.certificate.private_key.password";

    @Override
    public String getIbanityClientSslPrivateCertificatePathPropertyKey() {
        return IBANITY_CLIENT_SSL_PRIVATE_CERTIFICATE_PATH_PROPERTY_KEY;
    }

    @Override
    public String getIbanityClientSslPrivateCertificateIdPropertyKey() {
        return IBANITY_CLIENT_SSL_PRIVATE_CERTIFICATE_ID_PROPERTY_KEY;
    }

    @Override
    public String getIbanityClientSslPrivateCertificatePrivateKeyPathPropertyKey() {
        return IBANITY_CLIENT_SSL_PRIVATE_CERTIFICATE_PRIVATE_KEY_PATH_PROPERTY_KEY;
    }

    @Override
    public String getIbanityClientSslPrivateCertificatePrivateKeyPasswordPropertyKey() {
        return IBANITY_CLIENT_SSL_PRIVATE_CERTIFICATE_PRIVATE_KEY_PASSWORD_PROPERTY_KEY;
    }
}
