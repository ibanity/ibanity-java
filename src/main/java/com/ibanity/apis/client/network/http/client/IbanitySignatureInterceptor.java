package com.ibanity.apis.client.network.http.client;

import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.exceptions.InvalidDefaultHttpHeaderForSignatureException;
import com.ibanity.apis.client.exceptions.SignatureException;
import com.ibanity.apis.client.utils.FileUtils;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.protocol.HttpContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;

import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.util.Base64;
import java.util.stream.Stream;

public class IbanitySignatureInterceptor implements HttpRequestInterceptor {
    private static final Logger LOGGER = LogManager.getLogger(IbanitySignatureInterceptor.class);

    private static final String DIGEST_ALGORITHM                                                    = MessageDigestAlgorithms.SHA_256;
    private static final String IBANITY_HEADER_NAME_PREFIX                                          = "ibanity-";
    private static final String HEADER_SIGNATURE_HEADERS_NAME_SEPARATOR                             = " ";
    private static final String HEADER_NAME_DIGEST                                                  = "digest";
    private static final String HEADER_NAME_HOST                                                    = "host";
    private static final String HEADER_NAME_DATE                                                    = "date";
    private static final String HEADER_NAME_REQUEST_TARGET                                          = "(request-target)";
    private static final String DEFAULT_SIGNATURE_HEADERS_NAME                                      = HEADER_NAME_DIGEST
                                                                                                        + HEADER_SIGNATURE_HEADERS_NAME_SEPARATOR
                                                                                                        + HEADER_NAME_HOST
                                                                                                        + HEADER_SIGNATURE_HEADERS_NAME_SEPARATOR
                                                                                                        + HEADER_NAME_REQUEST_TARGET
                                                                                                        + HEADER_SIGNATURE_HEADERS_NAME_SEPARATOR
                                                                                                        + HEADER_NAME_DATE;

    private FileUtils fileUtils = new FileUtils();

    @Override
    public void process(final HttpRequest httpRequest, final HttpContext httpContext) throws HttpException, IOException {
        try {
            byte[] body = "".getBytes();
            HttpRequestWrapper requestWrapper = (HttpRequestWrapper) httpRequest;
            if (requestWrapper.getOriginal() instanceof HttpEntityEnclosingRequestBase) {
                body = IOUtils.toByteArray(((HttpEntityEnclosingRequestBase) requestWrapper.getOriginal()).getEntity().getContent());
            }
            setDefaultHttpHeaderValues(httpRequest, body);
            setSignatureHeader(httpRequest);

        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    private void setDefaultHttpHeaderValues(final HttpRequest httpRequest, final byte[] body) throws com.ibanity.apis.client.exceptions.DigestException, InvalidDefaultHttpHeaderForSignatureException {
        for (String headerName : DEFAULT_SIGNATURE_HEADERS_NAME.split(HEADER_SIGNATURE_HEADERS_NAME_SEPARATOR)) {
            switch (StringUtils.lowerCase(headerName)) {
                case HEADER_NAME_DIGEST:
                    setDigestHeader(httpRequest, body);
                    break;
                case HEADER_NAME_HOST:
                    break;
                case HEADER_NAME_REQUEST_TARGET:
                    break;
                case HEADER_NAME_DATE:
                    setDateHeader(httpRequest);
                    break;
                default:
                    throw new InvalidDefaultHttpHeaderForSignatureException();
            }
        }
    }

    private void setDateHeader(final HttpRequest httpRequest) {
        httpRequest.addHeader(HEADER_NAME_DATE, Instant.now().toString());
    }

    private String getRequestTargetHeaderValue(final HttpRequestWrapper requestWrapper) {
        return  StringUtils.lowerCase(requestWrapper.getRequestLine().getMethod())
                + HEADER_SIGNATURE_HEADERS_NAME_SEPARATOR
                + requestWrapper.getRequestLine().getUri();

    }

    private void setDigestHeader(final HttpRequest httpRequest, final byte[] body) throws com.ibanity.apis.client.exceptions.DigestException {
        String digestValue;
        try {
            digestValue = Base64.getUrlEncoder().encodeToString(MessageDigest.getInstance(DIGEST_ALGORITHM).digest(body));
            httpRequest.addHeader(HEADER_NAME_DIGEST, DIGEST_ALGORITHM + "=" + digestValue);
        } catch (NoSuchAlgorithmException e) {
            String errorMessage = "Signature Problem:" + DIGEST_ALGORITHM + ":is not available";
            LOGGER.fatal(errorMessage, e);
            throw new com.ibanity.apis.client.exceptions.DigestException(errorMessage, e);
        }
    }

    private void setSignatureHeader(final HttpRequest httpRequest) throws com.ibanity.apis.client.exceptions.SignatureException {
        HttpRequestWrapper requestWrapper = (HttpRequestWrapper) httpRequest;
        StringBuilder signatureHeaderValueBuilder = new StringBuilder();
        signatureHeaderValueBuilder
                .append("keyId=\"")
                .append(IbanityConfiguration.getConfiguration().getString(IbanityClientSecuritySignaturePropertiesKeys.IBANITY_CLIENT_SSL_PRIVATE_CERTIFICATE_ID_PROPERTY_KEY))
                .append("\"");
        signatureHeaderValueBuilder
                .append(HEADER_SIGNATURE_HEADERS_NAME_SEPARATOR)
                .append("algorithm=\"")
                .append(getSignatureAlgorithm(true))
                .append("\"");

        String signatureHeaders = getSignatureHeaders(requestWrapper);
        signatureHeaderValueBuilder
                .append(HEADER_SIGNATURE_HEADERS_NAME_SEPARATOR)
                .append("headers=\"")
                .append(signatureHeaders)
                .append("\"");
        signatureHeaderValueBuilder
                .append(HEADER_SIGNATURE_HEADERS_NAME_SEPARATOR)
                .append("signature=\"")
                .append(generateSignature(requestWrapper, signatureHeaders))
                .append("\"");

        requestWrapper.setHeader("signature", signatureHeaderValueBuilder.toString());
        LOGGER.debug("Signature Header value:" + signatureHeaderValueBuilder.toString() + ":");
    }

    private String generateSignatureString(final HttpRequestWrapper requestWrapper, final String signatureHeaders) {
        StringBuilder signature = new StringBuilder();
        Stream.of(StringUtils.split(signatureHeaders, HEADER_SIGNATURE_HEADERS_NAME_SEPARATOR))
                .forEach(headerName -> {
                    if (!StringUtils.equalsIgnoreCase(headerName, HEADER_NAME_REQUEST_TARGET)) {
                        Header header = requestWrapper.getFirstHeader(headerName);
                        signature.append(StringUtils.lowerCase(header.getName())).append(": ").append(header.getValue()).append("\n");
                    } else {
                        signature.append(StringUtils.lowerCase(headerName)).append(": ").append(getRequestTargetHeaderValue(requestWrapper)).append("\n");
                    }
                });
        signature.deleteCharAt(signature.lastIndexOf("\n"));
        LOGGER.debug("generateSignatureString:\n" + signature.toString() + "\n:");
        return signature.toString();
    }

    private String generateSignature(final HttpRequestWrapper requestWrapper, final String signatureHeaders) throws com.ibanity.apis.client.exceptions.SignatureException {
        try {
            String signatureString = generateSignatureString(requestWrapper, signatureHeaders);
            Signature signature = Signature.getInstance(getSignatureAlgorithm(false));
            signature.initSign(getCertificatePrivateKey());
            signature.update(signatureString.getBytes());
            byte[] signedData = signature.sign();

            String base64SignatureValue = Base64.getUrlEncoder().encodeToString(signedData);
            LOGGER.debug("Signature:" + base64SignatureValue);
            return base64SignatureValue;
        } catch (Exception e) {
            String errorMessage = "Error while trying to generate the signature of the request";
            LOGGER.fatal(errorMessage, e);
            throw new com.ibanity.apis.client.exceptions.SignatureException(errorMessage, e);
        }
    }

    private String getSignatureAlgorithm(final boolean forAlgorithmHeader) throws com.ibanity.apis.client.exceptions.SignatureException {
        KeyStore ks = IbanityHttpUtils.getCertificateKeyStore(new IbanityClientSecuritySignaturePropertiesKeys());
        String signatureAlgorithm;
        try {
            X509Certificate certificate = (X509Certificate) ks.getCertificateChain("application certificate")[0];
            signatureAlgorithm = certificate.getSigAlgName();
        } catch (KeyStoreException e) {
            String errorMessage = "Impossible to get the Certificate Signature Algorithm";
            LOGGER.fatal(errorMessage, e);
            throw new com.ibanity.apis.client.exceptions.SignatureException(errorMessage, e);
        }
        if (forAlgorithmHeader) {
            switch (signatureAlgorithm) {
                case "SHA256withRSA":
                    signatureAlgorithm = "rsa-sha256";
                    break;
                case "SHA512withRSA":
                    signatureAlgorithm = "rsa-sha512";
                    break;
                default:
                    throw new SignatureException("Unkown Signature Algorithm Property Value:" + signatureAlgorithm + ":");
            }
        }
        return signatureAlgorithm;
    }

    private PrivateKey getCertificatePrivateKey() throws IOException {
        Security.addProvider(new BouncyCastleProvider());

        try (
                PEMParser pemParser = new PEMParser(
                    new InputStreamReader(
                            fileUtils.loadFile(
                                    IbanityConfiguration.getConfiguration().getString(IbanityClientSecuritySignaturePropertiesKeys.IBANITY_CLIENT_SSL_PRIVATE_CERTIFICATE_PRIVATE_KEY_PATH_PROPERTY_KEY)
                            )
                    )
                )
            ) {
            PEMEncryptedKeyPair encryptedKeyPair = (PEMEncryptedKeyPair) pemParser.readObject();
            PEMDecryptorProvider decryptorProvider = new JcePEMDecryptorProviderBuilder().build(
                    IbanityConfiguration.getConfiguration().getString(IbanityClientSecuritySignaturePropertiesKeys.IBANITY_CLIENT_SSL_PRIVATE_CERTIFICATE_PRIVATE_KEY_PASSWORD_PROPERTY_KEY).toCharArray()
            );
            PEMKeyPair pemKeyPair = encryptedKeyPair.decryptKeyPair(decryptorProvider);

            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
            return converter.getPrivateKey(pemKeyPair.getPrivateKeyInfo());
        }
    }

    private String getSignatureHeaders(final HttpRequestWrapper requestWrapper) {
        StringBuilder headersValue = new StringBuilder();

        headersValue.append(DEFAULT_SIGNATURE_HEADERS_NAME);
        // Adding Authorization header if present
        Stream.of(requestWrapper.getAllHeaders()).filter(header -> StringUtils.equalsIgnoreCase(header.getName(), HttpHeaders.AUTHORIZATION)).findFirst().ifPresent(header -> headersValue.append(HEADER_SIGNATURE_HEADERS_NAME_SEPARATOR).append(StringUtils.lowerCase(header.getName())));

        // Adding Ibanity specific headers if present
        Stream.of(requestWrapper.getAllHeaders()).filter(header -> StringUtils.startsWithIgnoreCase(header.getName(), IBANITY_HEADER_NAME_PREFIX)).forEach(header -> headersValue.append(HEADER_SIGNATURE_HEADERS_NAME_SEPARATOR).append(StringUtils.lowerCase(header.getName())));

        LOGGER.debug("getSignatureHeaders value:" + headersValue + ":");
        return headersValue.toString();
    }
}
