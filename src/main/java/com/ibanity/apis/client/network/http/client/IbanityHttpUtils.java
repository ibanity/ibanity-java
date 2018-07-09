package com.ibanity.apis.client.network.http.client;

import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.utils.FileUtils;
import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.security.KeyStore;

public final class IbanityHttpUtils {
    private static final Logger LOGGER = LogManager.getLogger(IbanityHttpUtils.class);

    private static FileUtils fileUtils = new FileUtils();

    private IbanityHttpUtils() {
    }

    public static <T extends IbanityClientSecurityPropertiesKeys> SSLContext getSSLContext(final T clientProperties) {
        try {
            Configuration ibanityConfiguration = IbanityConfiguration.getConfiguration();
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(ibanityConfiguration.getString(IbanityClientSecurityPropertiesKeys.IBANITY_CLIENT_SSL_PRIVATE_CERTIFICATE_TRUSTMANAGER_PROPERTY_KEY));
            kmf.init(getCertificateKeyStore(clientProperties), ibanityConfiguration.getString(clientProperties.getIbanityClientSslPrivateCertificatePrivateKeyPasswordPropertyKey()).toCharArray());
            SSLContext sc = SSLContext.getInstance(ibanityConfiguration.getString(IbanityClientSecurityPropertiesKeys.IBANITY_CLIENT_SSL_PROTOCOL_PROPERTY_KEY));
            sc.init(kmf.getKeyManagers(), null, null);
            return sc;
        } catch (Exception e) {
            LOGGER.fatal(e);
            return null;
        }
    }

    public static <T extends IbanityClientSecurityPropertiesKeys> KeyStore getCertificateKeyStore(final T clientProperties) {
        try (FileInputStream fis = (FileInputStream) fileUtils.loadFile(IbanityConfiguration.getConfiguration().getString(clientProperties.getIbanityClientSslPrivateCertificatePathPropertyKey()))) {
            KeyStore ks = KeyStore.getInstance(IbanityConfiguration.getConfiguration().getString(IbanityClientSecurityPropertiesKeys.IBANITY_CLIENT_SSL_PRIVATE_CERTIFICATE_STANDARD_PROPERTY_KEY));
            char[] passwordCharArray = IbanityConfiguration.getConfiguration().getString(clientProperties.getIbanityClientSslPrivateCertificatePrivateKeyPasswordPropertyKey()).toCharArray();
            ks.load(fis, passwordCharArray);
            return ks;
        } catch (Exception e) {
            LOGGER.fatal(e);
        }
        return null;
    }
}
