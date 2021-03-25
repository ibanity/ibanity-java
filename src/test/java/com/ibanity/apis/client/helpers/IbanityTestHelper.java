package com.ibanity.apis.client.helpers;

import com.ibanity.apis.client.http.service.IbanityHttpSignatureService;
import com.ibanity.apis.client.http.service.impl.IbanityHttpSignatureServiceImpl;
import lombok.NonNull;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.iban4j.Iban;

import java.io.*;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;

public class IbanityTestHelper {

    public static final ProtocolVersion HTTP = new ProtocolVersion("HTTP", 0, 0);

    public static HttpResponse loadHttpResponse(String filePath) throws IOException {
        String jsonResponse = loadFile(filePath);
        HttpResponse httpResponse = new BasicHttpResponse(HTTP, 200, null);
        httpResponse.setEntity(new StringEntity(jsonResponse));
        return httpResponse;
    }

    public static HttpResponse createHttpResponse(String expected) throws UnsupportedEncodingException {
        HttpResponse postResponse = new BasicHttpResponse(HTTP, 200, null);
        postResponse.setEntity(new StringEntity(expected));
        return postResponse;
    }

    public static String loadFile(String filePath) throws IOException {
        return IOUtils.toString(
                requireNonNull(IbanityTestHelper.class.getClassLoader().getResourceAsStream(filePath)), UTF_8);
    }

    public static RSAPrivateKey readPrivateKey(String resource) {
        InputStream stream = IbanityTestHelper.class.getClassLoader().getResourceAsStream(resource);

        try (Reader keyReader = new BufferedReader(new InputStreamReader(stream));
             PemReader pemReader = new PemReader(keyReader)) {

            KeyFactory factory = KeyFactory.getInstance("RSA");
            PemObject pemObject = pemReader.readPemObject();
            byte[] content = pemObject.getContent();
            PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(content);

            return (RSAPrivateKey) factory.generatePrivate(privKeySpec);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static X509Certificate readCertificate(String resource) {
        InputStream stream = IbanityTestHelper.class.getClassLoader().getResourceAsStream(resource);
        CertificateFactory fact = null;
        try {
            fact = CertificateFactory.getInstance("X.509");
            return (X509Certificate) fact.generateCertificate(stream);
        } catch (CertificateException e) {
            throw new IllegalStateException(e);
        }
    }

    public static class IbanityHttpSignatureServiceBuilder {
        private PrivateKey privateKey = readPrivateKey("certificate/75b5d796-de5c-400a-81ce-e72371b01cbc-private_key.pem");
        private X509Certificate certificate = readCertificate("certificate/75b5d796-de5c-400a-81ce-e72371b01cbc-certificate.pem");
        private String certificateId = "75b5d796-de5c-400a-81ce-e72371b01cbc";
        private String ibanityEndpoint = "https://api.ibanity.com";

        public IbanityHttpSignatureServiceBuilder() {}

        public IbanityHttpSignatureServiceBuilder privateKey(String file) {
            this.privateKey = readPrivateKey(file);
            return this;
        }

        public IbanityHttpSignatureServiceBuilder certificate(String file) {
            this.certificate = readCertificate(file);
            return this;
        }

        public IbanityHttpSignatureServiceBuilder certificateId(String certificateId) {
            this.certificateId = certificateId;
            return this;
        }

        public IbanityHttpSignatureServiceBuilder ibanityEndpoint(String ibanityEndpoint) {
            this.ibanityEndpoint = ibanityEndpoint;
            return this;
        }

        public IbanityHttpSignatureService build() {
            return new IbanityHttpSignatureServiceImpl(
                    this.privateKey,
                    this.certificate,
                    this.certificateId,
                    this.ibanityEndpoint
            );
        }
    }
}