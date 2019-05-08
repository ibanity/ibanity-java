package com.ibanity.apis.client.network.http.client;

import com.ibanity.apis.client.utils.KeyToolHelper;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;

import static com.ibanity.apis.client.configuration.IbanityConfiguration.getConfiguration;
import static com.ibanity.apis.client.network.http.client.IbanityClientSecurityAuthenticationPropertiesKeys.IBANITY_CLIENT_SSL_CA_CERTIFICATE_PATH_PROPERTY_KEY;
import static com.ibanity.apis.client.network.http.client.IbanityClientSecurityAuthenticationPropertiesKeys.IBANITY_CLIENT_SSL_CLIENT_CERTIFICATE_PATH_PROPERTY_KEY;
import static com.ibanity.apis.client.network.http.client.IbanityClientSecurityAuthenticationPropertiesKeys.IBANITY_CLIENT_SSL_CLIENT_PRIVATE_KEY_PASSPHRASE_PROPERTY_KEY;
import static com.ibanity.apis.client.network.http.client.IbanityClientSecurityAuthenticationPropertiesKeys.IBANITY_CLIENT_SSL_CLIENT_PRIVATE_KEY_PATH_PROPERTY_KEY;

public final class IbanityHttpUtils {

    private static final String KEY_ENTRY_NAME = "application certificate";

    private IbanityHttpUtils() {
    }

    public static SSLContext getSSLContext()
            throws IOException, GeneralSecurityException {

        String privateKeyPassphrase = getConfiguration(IBANITY_CLIENT_SSL_CLIENT_PRIVATE_KEY_PASSPHRASE_PROPERTY_KEY, "");
        String sslProtocol = getConfiguration(IbanityClientSecurityPropertiesKeys.IBANITY_CLIENT_SSL_PROTOCOL_PROPERTY_KEY);

        KeyStore keyStore = createKeyStore(privateKeyPassphrase);
        KeyManager[] keyManagers = createKeyManagers(keyStore, privateKeyPassphrase);

        KeyStore trustStore = createTrustStore();
        TrustManager[] trustManagers = createTrustManagers(trustStore);


        SSLContext sslContext = SSLContext.getInstance(
                sslProtocol);
        sslContext.init(keyManagers, trustManagers, null);

        return sslContext;
    }

    private static TrustManager[] createTrustManagers(KeyStore trustStore) throws KeyStoreException, NoSuchAlgorithmException {
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);
        return trustManagerFactory.getTrustManagers();
    }

    private static KeyStore createTrustStore() throws GeneralSecurityException, IOException {
        Certificate certificate = KeyToolHelper.loadCertificate(
                getConfiguration(IBANITY_CLIENT_SSL_CA_CERTIFICATE_PATH_PROPERTY_KEY));

        KeyStore trustStore = KeyToolHelper.createKeyStore();

        if (!trustStore.containsAlias("ibanity-ca")) {
            trustStore.setCertificateEntry("ibanity-ca", certificate);
        }

        return trustStore;
    }

    private static KeyStore createKeyStore(String privateKeyPassphrase)
            throws IOException, GeneralSecurityException {


        PrivateKey privateKey = KeyToolHelper.loadPrivateKey(
                getConfiguration(IBANITY_CLIENT_SSL_CLIENT_PRIVATE_KEY_PATH_PROPERTY_KEY),
                privateKeyPassphrase);

        Certificate certificate = KeyToolHelper.loadCertificate(getConfiguration(IBANITY_CLIENT_SSL_CLIENT_CERTIFICATE_PATH_PROPERTY_KEY));

        KeyStore keyStore = KeyToolHelper.createKeyStore();

        KeyToolHelper.addEntryIfNotPresent(
                keyStore, KEY_ENTRY_NAME, privateKey, privateKeyPassphrase,
                new Certificate[]{certificate});

        return keyStore;
    }

    private static KeyManager[] createKeyManagers(KeyStore keyStore, String passphrase) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, passphrase.toCharArray());
        return kmf.getKeyManagers();
    }
}