package com.ibanity.apis.client.network.http.client;

import com.ibanity.apis.client.services.impl.IbanityHttpSignatureServiceImpl;
import com.ibanity.apis.client.utils.KeyToolHelper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.util.io.pem.PemReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static org.assertj.core.api.Assertions.assertThat;

class IbanityHttpSignatureServiceImplTest {

    private static final ZoneId ZONE_ID = ZoneId.of("UTC");
    private static final String ISO_DATE = "2019-01-30T09:51:57.124733Z";
    private static final String CERTIFICATE_ID = "75b5d796-de5c-400a-81ce-e72371b01cbc";
    private static final String IDEMPOTENCY_KEY = "61f02718-eeee-46e1-b5eb-e8fd6e799c2d";
    private static final String CERTIFICATE_FILENAME = "75b5d796-de5c-400a-81ce-e72371b01cbc-certificate.pem";
    private static final String PRIVATE_KEY_FILEMANE = "75b5d796-de5c-400a-81ce-e72371b01cbc-private_key.pem";
    private static final String EXPECTED_DIGEST = "SHA-512=pX9+OFjSGF4KFWUh8fv1Ihh4PuSb2KnyobO/hr228nkET5vRUhi0Qj2Ai5OcBXtzmzgII18sZiaEH4PoxkYqew==";
    private static final String EXPECTED_SIGNATURE_HEADERS = "(request-target) host digest date ibanity-idempotency-key";
    private static final String EXPECTED_SIGNATURE_VALUE = "Ys5Qj6sxRjp5x8/cTC7JXflueX8VtAExCHlKq3xQSQkUlB7bytbK5K00+AU+GIdmQt1tzWtNT/6Z5jkFaVPqliT6JXwa7SeuLuxHJjOwJ+pWVDoU1CiVjT5dtKw4vuabf0erftSX6G/XM5bpUR8M5fu8ObRQ2MaFxoQLjCAF5kfrfLdcpIIbl+ghKROBDmwKGdad0lxfab8I5tasr30/CwsHBDDIYQB1AzSfqfo1BjcudL9ALxukV+iEOzGlZJrhaSOHuKmjekNgPa+CUYIgU0eCMNAnL2zHcbQn2Nb0YwtO4p9Oxac2PnlCRz6IcNAwYwAw5hSttg7jJ17yj3ebeQ==";
    private static final String EXPECTED_SIGNATURE = "keyId=\"" + CERTIFICATE_ID + "\",algorithm=\"rsa-sha256\",headers=\"" + EXPECTED_SIGNATURE_HEADERS + "\",signature=\"" + EXPECTED_SIGNATURE_VALUE + "\"";

    private IbanityHttpSignatureServiceImpl httpSignatureService;

    private Clock clock = Clock.fixed(Instant.parse(ISO_DATE), ZONE_ID);

    @BeforeEach
    void setUp() throws Exception {
        httpSignatureService = new IbanityHttpSignatureServiceImpl(loadPrivateKey(), loadCertificate(), CERTIFICATE_ID, clock);
    }

    @Test
    void getHttpSignatureHeaders() throws MalformedURLException {
        Map<String, String> actual = httpSignatureService.getHttpSignatureHeaders("POST", new URL("https://api.ibanity.com/xs2a/customer-access-tokens?test=1&test=2"), getRequestHeaders(), getRequestPayload());
        assertThat(actual).isNotEmpty().hasSize(3);
        assertThat(actual).containsEntry("Date", ISO_DATE);
        assertThat(actual).containsEntry("Digest", EXPECTED_DIGEST);
        assertThat(actual).containsEntry("Signature", EXPECTED_SIGNATURE);
    }

    private HashMap<String, String> getRequestHeaders() {
        HashMap<String, String> headers = newHashMap();
        headers.put("Ibanity-Idempotency-key", IDEMPOTENCY_KEY);
        return headers;
    }

    private X509Certificate loadCertificate() throws CertificateException {
        return (X509Certificate) KeyToolHelper.loadCertificate(getPath(CERTIFICATE_FILENAME));
    }

    private PrivateKey loadPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String privateKeyPath = getPath(PRIVATE_KEY_FILEMANE);
        String privateKey = IOUtils.toString(FileUtils.openInputStream(new File(privateKeyPath)), "UTF-8");
        byte[] encoded = new PemReader(new StringReader(privateKey)).readPemObject().getContent();
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


}