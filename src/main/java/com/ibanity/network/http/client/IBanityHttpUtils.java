package com.ibanity.network.http.client;

import com.ibanity.api.configuration.IBanityConfiguration;
import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.security.KeyStore;

public class IBanityHttpUtils {
    private static final Logger LOGGER = LogManager.getLogger(IBanityHttpUtils.class);
    private static final String IBANITY_CLIENT_SSL_PRIVATE_CERTIFICATE_STANDARD_PROPERTY_KEY                = IBanityConfiguration.IBANITY_PROPERTIES_PREFIX + "client.ssl.private.certificate.standard";
    private static final String IBANITY_CLIENT_SSL_PRIVATE_CERTIFICATE_PATH_PROPERTY_KEY                    = IBanityConfiguration.IBANITY_PROPERTIES_PREFIX + "client.ssl.private.certificate.path";
    private static final String IBANITY_CLIENT_SSL_PRIVATE_CERTIFICATE_PRIVATE_KEY_PASSOWRD_PROPERTY_KEY    = IBanityConfiguration.IBANITY_PROPERTIES_PREFIX + "client.ssl.private.certificate.private_key.password";
    private static final String IBANITY_CLIENT_SSL_PRIVATE_CERTIFICATE_TRUSTMANAGER_PROPERTY_KEY            = IBanityConfiguration.IBANITY_PROPERTIES_PREFIX + "client.ssl.private.certificate.trustmanager";
    private static final String IBANITY_CLIENT_SSL_PROTOCOL_PROPERTY_KEY                                    = IBanityConfiguration.IBANITY_PROPERTIES_PREFIX + "client.ssl.protocol";

    public static SSLContext getSSLContext() {
        try {
            Configuration ibanityConfiguration = IBanityConfiguration.getConfiguration();
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(ibanityConfiguration.getString(IBANITY_CLIENT_SSL_PRIVATE_CERTIFICATE_TRUSTMANAGER_PROPERTY_KEY));
            kmf.init(getCertificateKeyStore(), ibanityConfiguration.getString(IBANITY_CLIENT_SSL_PRIVATE_CERTIFICATE_PRIVATE_KEY_PASSOWRD_PROPERTY_KEY).toCharArray());
            SSLContext sc = SSLContext.getInstance(ibanityConfiguration.getString(IBANITY_CLIENT_SSL_PROTOCOL_PROPERTY_KEY));
            sc.init(kmf.getKeyManagers(), null, null);
            return sc;
        } catch (Exception e) {
            LOGGER.fatal(e);
            return null;
        }
    }

    public static KeyStore getCertificateKeyStore(){
        try {
            Configuration ibanityConfiguration = IBanityConfiguration.getConfiguration();
            KeyStore ks = KeyStore.getInstance(ibanityConfiguration.getString(IBANITY_CLIENT_SSL_PRIVATE_CERTIFICATE_STANDARD_PROPERTY_KEY));
            FileInputStream fis = new FileInputStream(ibanityConfiguration.getString(IBANITY_CLIENT_SSL_PRIVATE_CERTIFICATE_PATH_PROPERTY_KEY));
            char[] passwordCharArray = ibanityConfiguration.getString(IBANITY_CLIENT_SSL_PRIVATE_CERTIFICATE_PRIVATE_KEY_PASSOWRD_PROPERTY_KEY).toCharArray();
            ks.load(fis, passwordCharArray);
            return ks;
        } catch (Exception e) {
            LOGGER.fatal(e);
            return null;
        }
    }
}
