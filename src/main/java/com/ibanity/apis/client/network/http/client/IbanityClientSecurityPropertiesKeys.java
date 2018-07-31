package com.ibanity.apis.client.network.http.client;

public abstract class IbanityClientSecurityPropertiesKeys {

    public static final String IBANITY_CLIENT_SSL_PROTOCOL_PROPERTY_KEY = "client.ssl.protocol";

    public abstract String getIbanityClientSslCertificatePathPropertyKey();

    public abstract String getIbanityClientSslPrivateKeyPathPropertyKey();

    public abstract String getIbanityClientSslPrivateKeyPassphrasePropertyKey();
}
