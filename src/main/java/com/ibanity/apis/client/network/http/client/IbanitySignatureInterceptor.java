package com.ibanity.apis.client.network.http.client;

import com.ibanity.apis.client.exceptions.InvalidDefaultHttpHeaderForSignatureException;
import com.ibanity.apis.client.exceptions.SignatureException;
import com.ibanity.apis.client.utils.KeyToolHelper;
import io.crnk.core.engine.http.HttpMethod;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.protocol.HttpContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.Assert;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.util.Base64;
import java.util.stream.Stream;

import static com.ibanity.apis.client.configuration.IbanityConfiguration.getConfiguration;

public class IbanitySignatureInterceptor implements HttpRequestInterceptor {
    private static final Logger LOGGER = LogManager.getLogger(IbanitySignatureInterceptor.class);

    private static final String DIGEST_ALGORITHM                         = MessageDigestAlgorithms.SHA_256;
    private static final String IBANITY_HEADER_NAME_PREFIX               = "ibanity-";
    private static final String HEADER_SIGNATURE_HEADERS_NAME_SEPARATOR  = " ";
    private static final String HEADER_NAME_DIGEST                       = "digest";
    private static final String HEADER_NAME_HOST                         = "host";
    private static final String HEADER_NAME_DATE                         = "date";
    private static final String HEADER_NAME_REQUEST_TARGET               = "(request-target)";
    private static final String HEADER_NAME_SIGNATURE                    = "signature";
    private static final String DEFAULT_SIGNATURE_HEADERS_NAME           =
            String.join(HEADER_SIGNATURE_HEADERS_NAME_SEPARATOR,
                        HEADER_NAME_DIGEST, HEADER_NAME_HOST,
                        HEADER_NAME_REQUEST_TARGET, HEADER_NAME_DATE);

    private final KeyToolHelper keyToolHelper = new KeyToolHelper();
    private final PrivateKey privateKey;
    private final X509Certificate certificate;
    private final String certificateId;
    private final String signatureHeaderTemplate;

    public IbanitySignatureInterceptor() {
        IbanityClientSecuritySignaturePropertiesKeys signaturePropertiesKeys = new IbanityClientSecuritySignaturePropertiesKeys();

        this.privateKey = this.getPrivateKey(signaturePropertiesKeys);

        this.certificate = this.getCertificate(signaturePropertiesKeys);

        this.certificateId = getConfiguration(signaturePropertiesKeys.getIbanityClientSslCertificateIdPropertyKey());

        this.signatureHeaderTemplate = this.getSignatureHeaderTemplate();
    }

    @Override
    public void process(final HttpRequest httpRequest, final HttpContext httpContext) throws IOException {
        try {
            HttpRequestWrapper requestWrapper = (HttpRequestWrapper) httpRequest;
            if (requestWrapper.getOriginal() instanceof HttpEntityEnclosingRequestBase) {
                byte[] body = IOUtils.toByteArray(
                        ((HttpEntityEnclosingRequestBase) requestWrapper.getOriginal())
                                .getEntity().getContent());
                this.setDefaultHttpHeaderValues(httpRequest, body);
            } else {
                this.setDefaultHttpHeaderValues(httpRequest, "".getBytes());
            }

            this.setSignatureHeader(httpRequest);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    private String getSignatureHeaderTemplate() {
        Assert.requireNonEmpty(certificateId, "CertificateId is not set");

        String signatureAlgorithm;
        try {
            signatureAlgorithm = getSignatureAlgorithmForHeader();
        } catch (InvalidAlgorithmParameterException e) {
            throw new IllegalArgumentException("Invalid Signature Algorithm", e);
        }

        return "keyId=\"" + certificateId + "\" "
                + "algorithm=\"" + signatureAlgorithm + "\" "
                + "headers=\"%s\" "
                + "signature=\"%s\"";
    }

    private X509Certificate getCertificate(final IbanityClientSecuritySignaturePropertiesKeys signaturePropertiesKeys) {
        String certificatePath = getConfiguration(signaturePropertiesKeys.getIbanityClientSslCertificatePathPropertyKey());
        try {
            return keyToolHelper.loadCertificate(certificatePath);
        } catch (CertificateException e) {
            throw new IllegalArgumentException("Invalid certificate configuration", e);
        }
    }

    private PrivateKey getPrivateKey(final IbanityClientSecuritySignaturePropertiesKeys signaturePropertiesKeys) {
        String privateKeyPath = getConfiguration(signaturePropertiesKeys.getIbanityClientSslPrivateKeyPathPropertyKey());
        char[] privateKeyPassPhrase = getConfiguration(signaturePropertiesKeys.getIbanityClientSslPrivateKeyPassphrasePropertyKey()).toCharArray();
        try {
            return keyToolHelper.loadPrivateKey(privateKeyPath, privateKeyPassPhrase);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid private key configuration", e);
        }
    }

    private String getRequestTargetHeaderValue(final HttpRequestWrapper requestWrapper) {
        return StringUtils.lowerCase(requestWrapper.getRequestLine().getMethod())
                + " "
                + requestWrapper.getRequestLine().getUri();
    }

    private String getSignatureAlgorithm() {
        return this.certificate.getSigAlgName();
    }

    private String getSignatureAlgorithmForHeader() throws InvalidAlgorithmParameterException {
        String signatureAlgorithm = this.getSignatureAlgorithm();

        switch (signatureAlgorithm) {
            case "SHA256withRSA":
                return "rsa-sha256";
            case "SHA512withRSA":
                return "rsa-sha512";
            default:
                throw new InvalidAlgorithmParameterException("Unsupported signature algorithm:" + signatureAlgorithm);
        }
    }

    private String getSignatureHeaders(final HttpRequestWrapper requestWrapper) {
        StringBuilder headersValue = new StringBuilder();

        headersValue.append(DEFAULT_SIGNATURE_HEADERS_NAME);

        Stream.of(requestWrapper.getAllHeaders())
                .filter(header -> StringUtils.equalsIgnoreCase(header.getName(), HttpHeaders.AUTHORIZATION))
                .findFirst()
                .ifPresent(header -> headersValue.append(HEADER_SIGNATURE_HEADERS_NAME_SEPARATOR)
                        .append(StringUtils.lowerCase(header.getName())));

        Stream.of(requestWrapper.getAllHeaders())
                .filter(header -> StringUtils.startsWithIgnoreCase(header.getName(), IBANITY_HEADER_NAME_PREFIX))
                .forEach(header -> headersValue.append(HEADER_SIGNATURE_HEADERS_NAME_SEPARATOR)
                        .append(StringUtils.lowerCase(header.getName())));

        return headersValue.toString();
    }

    private void setDefaultHttpHeaderValues(final HttpRequest httpRequest, final byte[] body) throws com.ibanity.apis.client.exceptions.DigestException, InvalidDefaultHttpHeaderForSignatureException {
        for (String headerName : DEFAULT_SIGNATURE_HEADERS_NAME.split(HEADER_SIGNATURE_HEADERS_NAME_SEPARATOR)) {
            switch (StringUtils.lowerCase(headerName)) {
                case HEADER_NAME_DIGEST:
                    setDigestHeader(httpRequest, body);
                    break;
                case HEADER_NAME_DATE:
                    setDateHeader(httpRequest);
                    break;
                case HEADER_NAME_HOST:
                case HEADER_NAME_REQUEST_TARGET:
                    break;
                default:
                    throw new InvalidDefaultHttpHeaderForSignatureException();
            }
        }
    }

    private void setDateHeader(final HttpRequest httpRequest) {
        httpRequest.addHeader(HEADER_NAME_DATE, Instant.now().toString());
    }

    private void setDigestHeader(final HttpRequest httpRequest, final byte[] body) throws com.ibanity.apis.client.exceptions.DigestException {
        String digestValue;
        try {
            digestValue = Base64.getUrlEncoder().encodeToString(MessageDigest.getInstance(DIGEST_ALGORITHM).digest(body));
            httpRequest.addHeader(HEADER_NAME_DIGEST, DIGEST_ALGORITHM + "=" + digestValue);
        } catch (NoSuchAlgorithmException e) {
            String errorMessage = "Failed to set the Digest Http header: " + DIGEST_ALGORITHM + " is not available";
            LOGGER.fatal(errorMessage, e);
            throw new com.ibanity.apis.client.exceptions.DigestException(errorMessage, e);
        }
    }

    private void setSignatureHeader(final HttpRequest httpRequest) throws SignatureException {
        HttpRequestWrapper requestWrapper = (HttpRequestWrapper) httpRequest;

        String signatureHeaders = this.getSignatureHeaders(requestWrapper);
        String signatureValue = this.generateSignature(requestWrapper, signatureHeaders);

        String requestTarget = this.getRequestTargetHeaderValue(requestWrapper);

        if (HttpMethod.POST.name().equalsIgnoreCase(requestTarget)
                || HttpMethod.PATCH.name().equalsIgnoreCase(requestTarget)) {
            requestWrapper.setHeader(HEADER_NAME_SIGNATURE,
                    String.format(signatureHeaderTemplate, signatureHeaders, signatureValue));
        }
    }

    private String generateSignatureString(final HttpRequestWrapper requestWrapper, final String signatureHeaders) {
        StringBuilder signature = new StringBuilder();
        Stream.of(StringUtils.split(signatureHeaders, HEADER_SIGNATURE_HEADERS_NAME_SEPARATOR))
                .forEach(headerName -> {
                    signature
                            .append(StringUtils.lowerCase(headerName))
                            .append(": ");
                    if (!StringUtils.equalsIgnoreCase(headerName, HEADER_NAME_REQUEST_TARGET)) {
                        Header header = requestWrapper.getFirstHeader(headerName);
                        signature.append(header.getValue());
                    } else {
                        signature.append(getRequestTargetHeaderValue(requestWrapper));
                    }
                    signature.append("\n");
                });
        signature.deleteCharAt(signature.lastIndexOf("\n"));
        return signature.toString();
    }

    private String generateSignature(final HttpRequestWrapper requestWrapper, final String signatureHeaders) throws SignatureException {
        try {
            String signatureString = generateSignatureString(requestWrapper, signatureHeaders);
            Signature signature = Signature.getInstance(this.getSignatureAlgorithm());
            signature.initSign(privateKey);
            signature.update(signatureString.getBytes());
            byte[] signedData = signature.sign();

            return Base64.getEncoder().encodeToString(signedData);
        } catch (Exception e) {
            String errorMessage = "Error while trying to generate the signature of the request";
            LOGGER.fatal(errorMessage, e);
            throw new SignatureException(errorMessage, e);
        }
    }

}
