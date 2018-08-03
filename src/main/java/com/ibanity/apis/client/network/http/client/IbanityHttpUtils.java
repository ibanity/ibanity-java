package com.ibanity.apis.client.network.http.client;

import com.ibanity.apis.client.utils.KeyToolHelper;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509KeyManager;
import java.io.IOException;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import static com.ibanity.apis.client.configuration.IbanityConfiguration.getConfiguration;

public final class IbanityHttpUtils {
    private IbanityHttpUtils() {
    }

    private static X509KeyManager getX509KeyManager(final KeyManagerFactory kmf) {
        final X509KeyManager origKm = (X509KeyManager) kmf.getKeyManagers()[0];

        return new X509KeyManager() {
            @Override
            public String[] getClientAliases(final String s, final Principal[] principals) {
                return origKm.getClientAliases(s, principals);
            }

            public String chooseClientAlias(final String[] keyType, final Principal[] issuers, final Socket socket) {
                return "application certificate";
            }

            @Override
            public String[] getServerAliases(final String s, final Principal[] principals) {
                return origKm.getServerAliases(s, principals);
            }

            @Override
            public String chooseServerAlias(final String s, final Principal[] principals, final Socket socket) {
                return origKm.chooseServerAlias(s, principals, socket);
            }

            public X509Certificate[] getCertificateChain(final String alias) {
                return origKm.getCertificateChain(alias);
            }

            @Override
            public PrivateKey getPrivateKey(final String s) {
                return origKm.getPrivateKey(s);
            }
        };
    }

    public static <T extends IbanityClientSecurityPropertiesKeys> SSLContext getSSLContext()
            throws IOException, GeneralSecurityException {

        IbanityClientSecurityAuthenticationPropertiesKeys authenticationPropertiesKeys =
                new IbanityClientSecurityAuthenticationPropertiesKeys();

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(
                createKeyStore(authenticationPropertiesKeys),
                getConfiguration(authenticationPropertiesKeys.getIbanityClientPrivateKeyPassphrasePropertyKey()).toCharArray()
        );

        SSLContext sslContext = SSLContext.getInstance(
                getConfiguration(IbanityClientSecurityPropertiesKeys.IBANITY_CLIENT_SSL_PROTOCOL_PROPERTY_KEY));
        sslContext.init(new KeyManager[]{getX509KeyManager(kmf)}, null, null);

        return sslContext;
    }

    private static <T extends IbanityClientSecurityPropertiesKeys> KeyStore createKeyStore(final T clientProperties)
            throws IOException, GeneralSecurityException {

        KeyToolHelper keyToolHelper = new KeyToolHelper();

        PrivateKey privateKey = keyToolHelper.loadPrivateKey(
                getConfiguration(clientProperties.getIbanityClientPrivateKeyPathPropertyKey()),
                getConfiguration(clientProperties.getIbanityClientPrivateKeyPassphrasePropertyKey()).toCharArray()
        );

        X509Certificate certificate = keyToolHelper.loadCertificate(
                getConfiguration(clientProperties.getIbanityClientCertificatePathPropertyKey())
        );

        KeyStore keyStore = keyToolHelper.createKeyStore();

        keyToolHelper.addEntryIfNotPresent(
                keyStore, "application certificate", privateKey,
                new X509Certificate[]{certificate});

        return keyStore;
    }
}
