package com.ibanity.apis.client.network.http.client;

public abstract class IbanityClientSecurityPropertiesKeys {

    public static final String IBANITY_CLIENT_SSL_PROTOCOL_PROPERTY_KEY = "client.ssl.protocol";

    public abstract String getIbanityClientCertificatePathPropertyKey();

    public abstract String getIbanityClientPrivateKeyPathPropertyKey();

    public abstract String getIbanityClientPrivateKeyPassphrasePropertyKey();
}
