package com.ibanity.apis.client.http.service.impl;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

class IbanityHttpSignatureServiceImplTest {

    private static final ZoneId ZONE_ID = ZoneId.of("UTC");
    private static final String ISO_DATE = "2019-01-30T09:51:57.124733Z";
    private static final Clock CLOCK = Clock.fixed(Instant.parse(ISO_DATE), ZONE_ID);

    private static final String CERTIFICATE_ID = "75b5d796-de5c-400a-81ce-e72371b01cbc";
    private static final String IDEMPOTENCY_KEY = "61f02718-eeee-46e1-b5eb-e8fd6e799c2d";
    private static final String CERTIFICATE_FILENAME = "75b5d796-de5c-400a-81ce-e72371b01cbc-certificate.pem";
    private static final String PRIVATE_KEY_FILENAME = "75b5d796-de5c-400a-81ce-e72371b01cbc-private_key.pem";

    private static final String EXPECTED_DIGEST = "SHA-512=pX9+OFjSGF4KFWUh8fv1Ihh4PuSb2KnyobO/hr228nkET5vRUhi0Qj2Ai5OcBXtzmzgII18sZiaEH4PoxkYqew==";
    private static final String EXPECTED_SIGNING_STRING = String.join("\n",
            "(request-target): post /xs2a/customer-access-tokens?test=1&test=2",
            "host: api.ibanity.com",
            "digest: SHA-512=pX9+OFjSGF4KFWUh8fv1Ihh4PuSb2KnyobO/hr228nkET5vRUhi0Qj2Ai5OcBXtzmzgII18sZiaEH4PoxkYqew==",
            "(created): 1548841917",
            "ibanity-idempotency-key: 61f02718-eeee-46e1-b5eb-e8fd6e799c2d"
    );
    public static final String IBANITY_ENDPOINT = "https://api.ibanity.com";

    private IbanityHttpSignatureServiceImpl httpSignatureService;


    @BeforeEach
    void setUp() throws Exception {
        httpSignatureService = new IbanityHttpSignatureServiceImpl(loadPrivateKey(), loadCertificate(), CERTIFICATE_ID, CLOCK, "https://api.ibanity.com/");
    }

    @Test
    void getHttpSignatureHeaders() throws MalformedURLException {
        Map<String, String> actual = getSignatureHeaders(IBANITY_ENDPOINT);
        assertThat(actual).isNotEmpty().hasSize(2);
        assertThat(actual).containsEntry("Digest", EXPECTED_DIGEST);
        assertThat(actual).containsKey("Signature");
    }

    @Test
    void verifySignature_whenProxyIsNull() throws Exception {
        httpSignatureService = new IbanityHttpSignatureServiceImpl(loadPrivateKey(), loadCertificate(), CERTIFICATE_ID, CLOCK, "https://api.ibanity.com/", null);
        Map<String, String> actual = getSignatureHeaders(IBANITY_ENDPOINT);

        Signature publicSignature = Signature.getInstance("RSASSA-PSS");
        publicSignature.setParameter(IbanityHttpSignatureServiceImpl.PARAMETER_SPEC);

        PublicKey publicKey = loadPublicKey();
        publicSignature.initVerify(publicKey);

        publicSignature.update(EXPECTED_SIGNING_STRING.getBytes(UTF_8));

        String signaturePart = getSignaturePart(actual);
        byte[] signatureBytes = Base64.getDecoder().decode(signaturePart);

        assert publicSignature.verify(signatureBytes);
    }

    @Test
    void verifySignature_whenProxyIsHost() throws Exception {
        String proxyUrl = "https://myproxy.com";

        httpSignatureService = new IbanityHttpSignatureServiceImpl(loadPrivateKey(), loadCertificate(), CERTIFICATE_ID, CLOCK, "https://api.ibanity.com/", proxyUrl);
        Map<String, String> actual = getSignatureHeaders(proxyUrl);

        Signature publicSignature = Signature.getInstance("RSASSA-PSS");
        publicSignature.setParameter(IbanityHttpSignatureServiceImpl.PARAMETER_SPEC);

        PublicKey publicKey = loadPublicKey();
        publicSignature.initVerify(publicKey);

        publicSignature.update(EXPECTED_SIGNING_STRING.getBytes(UTF_8));

        String signaturePart = getSignaturePart(actual);
        byte[] signatureBytes = Base64.getDecoder().decode(signaturePart);

        assert publicSignature.verify(signatureBytes);
    }

    @Test
    void verifySignature_whenCustomProxyPath() throws Exception {
        String proxyUrl = "http://my-proxy/rewriting-the-path";
        httpSignatureService = new IbanityHttpSignatureServiceImpl(loadPrivateKey(), loadCertificate(), CERTIFICATE_ID, CLOCK, "https://api.ibanity.com/", proxyUrl);

        Map<String, String> actual = getSignatureHeaders(proxyUrl);

        Signature publicSignature = Signature.getInstance("RSASSA-PSS");
        publicSignature.setParameter(IbanityHttpSignatureServiceImpl.PARAMETER_SPEC);

        PublicKey publicKey = loadPublicKey();
        publicSignature.initVerify(publicKey);

        publicSignature.update(EXPECTED_SIGNING_STRING.getBytes(UTF_8));

        String signaturePart = getSignaturePart(actual);
        byte[] signatureBytes = Base64.getDecoder().decode(signaturePart);

        assert publicSignature.verify(signatureBytes);
    }

    private String getSignaturePart(Map<String, String> httpSignatureHeaders) {
        String[] signatureParts = httpSignatureHeaders.get("Signature").split(",\\w+=");
        return signatureParts[4].replace("\"", "");
    }

    private Map<String, String> getRequestHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Ibanity-Idempotency-key", IDEMPOTENCY_KEY);
        return headers;
    }

    private PublicKey loadPublicKey() throws CertificateException {
        X509Certificate certificate = loadCertificate();
        return certificate.getPublicKey();
    }

    private X509Certificate loadCertificate() throws CertificateException {

        return (X509Certificate) loadCertificate(getPath(CERTIFICATE_FILENAME));
    }

    private PrivateKey loadPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String privateKeyPath = getPath(PRIVATE_KEY_FILENAME);
        String privateKey = IOUtils.toString(FileUtils.openInputStream(new File(privateKeyPath)), UTF_8);
        String privateKeyPEM = privateKey
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PRIVATE KEY-----", "");

        byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);
        return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(encoded));
    }

    private String getPath(String filename) {
        URL resource = getClass().getClassLoader().getResource("certificate/" + filename);
        return resource.getPath();
    }

    private String getRequestPayload() {
        //language=JSON
        return "{\"msg\":\"hello\"}";
    }

    private Map<String, String> getSignatureHeaders(String host) throws MalformedURLException {
        URL url = new URL(host + "/xs2a/customer-access-tokens?test=1&test=2");
        return httpSignatureService.getHttpSignatureHeaders("POST", url, getRequestHeaders(), getRequestPayload());
    }

    public static Certificate loadCertificate(final String certificatePath) throws CertificateException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        try {
            InputStream is = FileUtils.openInputStream(new File(certificatePath));
            return certificateFactory.generateCertificate(is);
        } catch (IOException exception) {
            throw new IllegalArgumentException(new FileNotFoundException("Resource Path not found:" + certificatePath));
        }
    }
}
