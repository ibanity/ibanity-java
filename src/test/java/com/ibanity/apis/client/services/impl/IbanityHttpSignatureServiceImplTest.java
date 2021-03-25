package com.ibanity.apis.client.services.impl;

import com.ibanity.apis.client.helpers.IbanityTestHelper;
import com.ibanity.apis.client.http.service.IbanityHttpSignatureService;
import com.ibanity.apis.client.http.service.impl.IbanityHttpSignatureServiceImpl;
import org.apache.commons.io.IOUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static com.ibanity.apis.client.helpers.IbanityTestHelper.IbanityHttpSignatureServiceBuilder;
import static com.ibanity.apis.client.helpers.IbanityTestHelper.loadFile;

public class IbanityHttpSignatureServiceImplTest {

    private IbanityHttpSignatureService signatureService = new IbanityHttpSignatureServiceBuilder().build();

    @Test
    public void testGetDigestHeaderWithEmptyString() {
        InputStream payload = IOUtils.toInputStream("", StandardCharsets.UTF_8);
        String actual = IbanityHttpSignatureServiceImpl.getDigestHeader(payload);
        String expected = "SHA-512=z4PhNX7vuL3xVChQ1m2AB9Yg5AULVxXcg/SpIdNs6c5H0NE8XYXysP+DGNKHfuwvY7kxvUdBeoGlODJ6+SfaPg==";

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testGetDigestHeaderWithString() throws IOException {
        InputStream payload = IOUtils.toInputStream(loadFile("coda/coda-sample.txt"), StandardCharsets.UTF_8);
        String actual = IbanityHttpSignatureServiceImpl.getDigestHeader(payload);
        String expected = "SHA-512=Gjmcmw0tVwWRCesJFlqvMjkqw9fjBlhKIx/8lgosqlLbmActJWYAetZU8kTUIxBLp+Tlu8KhfVkg4rVmbnt1ZA==";

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testGetDigestHeaderWithFileInputStream() throws IOException {
        InputStream payload = IbanityTestHelper.class.getClassLoader().getResourceAsStream("coda/coda-sample.txt");
        String actual = IbanityHttpSignatureServiceImpl.getDigestHeader(payload);
        String expected = "SHA-512=Gjmcmw0tVwWRCesJFlqvMjkqw9fjBlhKIx/8lgosqlLbmActJWYAetZU8kTUIxBLp+Tlu8KhfVkg4rVmbnt1ZA==";

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testGetSignatureHeaders() throws IOException {
        File file = new File("src/test/resources/coda/coda-sample.txt");
        URL url = new URL("http://www.example.com");
        HashMap<String, String> headers = new HashMap<>();
        Map<String, String> requestHeaders = signatureService.getHttpSignatureHeaders("POST", url, headers, file);

        String expected = "SHA-512=Gjmcmw0tVwWRCesJFlqvMjkqw9fjBlhKIx/8lgosqlLbmActJWYAetZU8kTUIxBLp+Tlu8KhfVkg4rVmbnt1ZA==";
        Assertions.assertThat(requestHeaders.get("Digest")).isEqualTo(expected);
    }
}
