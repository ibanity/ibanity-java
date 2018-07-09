package com.ibanity.apis.client.network.http.client;

import com.ibanity.apis.client.configuration.IbanityConfiguration;

public abstract class IbanityClientSecurityPropertiesKeys {

    public static final String IBANITY_CLIENT_SSL_PRIVATE_CERTIFICATE_STANDARD_PROPERTY_KEY = IbanityConfiguration.IBANITY_PROPERTIES_PREFIX + "client.ssl.certificate.standard";
    public static final String IBANITY_CLIENT_SSL_PRIVATE_CERTIFICATE_TRUSTMANAGER_PROPERTY_KEY = IbanityConfiguration.IBANITY_PROPERTIES_PREFIX + "client.ssl.certificate.trustmanager";
    public static final String IBANITY_CLIENT_SSL_PROTOCOL_PROPERTY_KEY = IbanityConfiguration.IBANITY_PROPERTIES_PREFIX + "client.ssl.protocol";

    public abstract String getIbanityClientSslPrivateCertificatePathPropertyKey();

    public abstract String getIbanityClientSslPrivateCertificateIdPropertyKey();

    public abstract String getIbanityClientSslPrivateCertificatePrivateKeyPathPropertyKey();

    public abstract String getIbanityClientSslPrivateCertificatePrivateKeyPasswordPropertyKey();
}
