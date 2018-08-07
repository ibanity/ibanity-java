package com.ibanity.apis.client.utils;

import com.ibanity.apis.client.network.http.client.IbanityClientSecurityAuthenticationPropertiesKeys;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidator;
import java.security.cert.CertPathValidatorException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.PKIXParameters;
import java.security.cert.X509Certificate;
import java.util.List;

import static com.ibanity.apis.client.configuration.IbanityConfiguration.getConfiguration;

public final class KeyToolHelper {

    private final FileUtils fileUtils = new FileUtils();

    public static final String CERTIFICATE_FACTORY_X509_TYPE = "X.509";
    public static final String CERT_PATH_VALIDATOR_PKIX = "PKIX";

    private static final CertificateFactory CERTIFICATE_FACTORY_INSTANCE;
    private static final CertPathValidator CERT_PATH_VALIDATOR_INSTANCE;
    private static final String BOUNCY_CASTLE_PROVIDER = "BC";

    static {
        Security.addProvider(new BouncyCastleProvider());

        try {
            CERTIFICATE_FACTORY_INSTANCE = CertificateFactory.getInstance(CERTIFICATE_FACTORY_X509_TYPE);
        } catch (CertificateException e) {
            throw new IllegalStateException("Unable to create CERTIFICATE_FACTORY_INSTANCE");
        }
        try {
           CERT_PATH_VALIDATOR_INSTANCE = CertPathValidator.getInstance(CERT_PATH_VALIDATOR_PKIX);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Unable to create certPathValidator");
        }
    }

    private final JcaPEMKeyConverter jcaPEMKeyConverter = new JcaPEMKeyConverter().setProvider(BOUNCY_CASTLE_PROVIDER);

    private static CertificateFactory getCertificateFactory() {
        return CERTIFICATE_FACTORY_INSTANCE;
    }

    private static CertPathValidator getCertPathValidator() {
        return CERT_PATH_VALIDATOR_INSTANCE;
    }

    public KeyStore createKeyStore() throws GeneralSecurityException, IOException {
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null);
        return keyStore;
    }

    public KeyStore addTrustedCertificateIfNotPresent(final KeyStore keyStore, final X509Certificate trustedCertificate)
            throws KeyStoreException {

        if (keyStore.containsAlias(trustedCertificate.getSerialNumber().toString())) {
            return keyStore;
        }

        keyStore.setCertificateEntry(trustedCertificate.getSerialNumber().toString(), trustedCertificate);

        return keyStore;
    }

    public boolean containsEntry(final KeyStore keyStore, final String alias) throws KeyStoreException {
        return keyStore.containsAlias(alias);
    }

    public KeyStore addEntryIfNotPresent(final KeyStore keyStore, final String alias,
                                         final PrivateKey privateKey, final X509Certificate[] certChain)
            throws GeneralSecurityException {

        if (keyStore.containsAlias(alias)) {
            return keyStore;
        }

        IbanityClientSecurityAuthenticationPropertiesKeys authenticationPropertiesKeys =
                new IbanityClientSecurityAuthenticationPropertiesKeys();

        keyStore.setKeyEntry(
                alias, privateKey,
                getConfiguration(authenticationPropertiesKeys.getIbanityClientPrivateKeyPassphrasePropertyKey()).toCharArray(),
                certChain);

        return keyStore;
    }

    public void validateCertificatePath(final List<? extends Certificate> certificates,
                                        final PKIXParameters pkixParameters)
            throws CertificateException, CertPathValidatorException, InvalidAlgorithmParameterException {
        CertPath certPath = getCertificateFactory().generateCertPath(certificates);

        getCertPathValidator().validate(certPath, pkixParameters);
    }

    public X509Certificate loadCertificate(final String certificatePath) throws CertificateException {
        InputStream is = fileUtils.getInputStream(certificatePath);

        return this.toCertificate(is);
    }

    public X509Certificate toCertificate(final InputStream is) throws CertificateException {
        return (X509Certificate) getCertificateFactory().generateCertificate(is);
    }

    public X509Certificate toCertificate(final String s) throws CertificateException {
        return this.toCertificate(new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8)));
    }

    public X509Certificate[] loadCertificates(final String certificatePath) throws CertificateException, IOException {
        try (InputStream is = fileUtils.getInputStream(certificatePath)) {
            return this.toCertificates(is);
        }
    }

    public X509Certificate[] toCertificates(final InputStream is) throws CertificateException {
        return getCertificateFactory().generateCertificates(is).toArray(new X509Certificate[0]);
    }

    public X509Certificate[] toCertificates(final String s) throws CertificateException {
        return this.toCertificates(new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8)));
    }

    public PrivateKey loadPrivateKey(final String privateKeyPath, final char[] privateKeyPassPhrase) throws IOException {
        try (InputStream is = fileUtils.getInputStream(privateKeyPath)) {
            byte[] fileBytes = IOUtils.toByteArray(is);

            return this.toPrivateKey(fileBytes, privateKeyPassPhrase);
        }
    }

    public PrivateKey toPrivateKey(final byte[] keyBytes, final char[] privateKeyPassPhrase) throws IOException {
        return this.toPrivateKey(new String(keyBytes), privateKeyPassPhrase);
    }

    public PrivateKey toPrivateKey(final String privateKeyPem, final char[] privateKeyPassPhrase) throws IOException {
        PEMEncryptedKeyPair encryptedKeyPair = (PEMEncryptedKeyPair) new PEMParser(new StringReader(privateKeyPem)).readObject();

        PEMDecryptorProvider decryptorProvider = new JcePEMDecryptorProviderBuilder()
                .build(privateKeyPassPhrase);

        PEMKeyPair pemKeyPair = encryptedKeyPair.decryptKeyPair(decryptorProvider);

        jcaPEMKeyConverter.setProvider(BOUNCY_CASTLE_PROVIDER);
        return jcaPEMKeyConverter.getPrivateKey(pemKeyPair.getPrivateKeyInfo());
    }

}
