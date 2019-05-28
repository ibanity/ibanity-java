package com.ibanity.apis.client.helpers;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

public final class KeyToolHelper {

    private static final String CERTIFICATE_FACTORY_X509_TYPE = "X.509";

    private static final CertificateFactory CERTIFICATE_FACTORY_INSTANCE;
    private static final String BOUNCY_CASTLE_PROVIDER = "BC";

    static {
        Security.addProvider(new BouncyCastleProvider());

        try {
            CERTIFICATE_FACTORY_INSTANCE = CertificateFactory.getInstance(CERTIFICATE_FACTORY_X509_TYPE);
        } catch (CertificateException exception) {
            throw new IllegalStateException("Unable to create CERTIFICATE_FACTORY_INSTANCE");
        }
    }

    public static Certificate loadCertificate(final String certificatePath) throws CertificateException {
        try {
            InputStream is = FileUtils.openInputStream(new File(certificatePath));
            return getCertificateFactory().generateCertificate(is);
        } catch (IOException exception) {
            throw new IllegalArgumentException(new FileNotFoundException("Resource Path not found:" + certificatePath));
        }
    }

    public static PrivateKey loadPrivateKey(String privateKeyPath, String privateKeyPassPhrase) throws IOException {
        try (InputStream is = FileUtils.openInputStream(new File(privateKeyPath))) {
            String privateKeyPem = new String(IOUtils.toByteArray(is));

            PEMEncryptedKeyPair encryptedKeyPair = (PEMEncryptedKeyPair) new PEMParser(new StringReader(privateKeyPem)).readObject();

            PEMDecryptorProvider decryptorProvider = new JcePEMDecryptorProviderBuilder()
                    .build(privateKeyPassPhrase.toCharArray());

            PEMKeyPair pemKeyPair = encryptedKeyPair.decryptKeyPair(decryptorProvider);
            JcaPEMKeyConverter jcaPEMKeyConverter = new JcaPEMKeyConverter().setProvider(BOUNCY_CASTLE_PROVIDER);
            jcaPEMKeyConverter.setProvider(BOUNCY_CASTLE_PROVIDER);
            return jcaPEMKeyConverter.getPrivateKey(pemKeyPair.getPrivateKeyInfo());
        }
    }

    private static CertificateFactory getCertificateFactory() {
        return CERTIFICATE_FACTORY_INSTANCE;
    }
}
