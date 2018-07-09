package com.ibanity.apis.client.network.http.client;

import com.ibanity.apis.client.configuration.IbanityConfiguration;

public interface IbanityClientSecurityPropertiesKeys {

    String IBANITY_CLIENT_SSL_PRIVATE_CERTIFICATE_STANDARD_PROPERTY_KEY = IbanityConfiguration.IBANITY_PROPERTIES_PREFIX + "client.ssl.certificate.standard";
    String IBANITY_CLIENT_SSL_PRIVATE_CERTIFICATE_TRUSTMANAGER_PROPERTY_KEY = IbanityConfiguration.IBANITY_PROPERTIES_PREFIX + "client.ssl.certificate.trustmanager";
    String IBANITY_CLIENT_SSL_PROTOCOL_PROPERTY_KEY = IbanityConfiguration.IBANITY_PROPERTIES_PREFIX + "client.ssl.protocol";

    String getIbanityClientSslPrivateCertificatePathPropertyKey();

    String getIbanityClientSslPrivateCertificateIdPropertyKey();

    String getIbanityClientSslPrivateCertificatePrivateKeyPathPropertyKey();

    String getIbanityClientSslPrivateCertificatePrivateKeyPasswordPropertyKey();
}
