package com.ibanity.apis.client.http.service.impl;

import com.ibanity.apis.client.http.service.IbanityHttpSignatureService;
import lombok.NonNull;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.X509Certificate;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import java.time.Clock;
import java.time.Instant;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class IbanityHttpSignatureServiceImpl implements IbanityHttpSignatureService {

    public static final int BUFFER_SIZE = 32_768;
    public static final String SIGNATURE_ALGORITHM = "RSASSA-PSS";
    public static final PSSParameterSpec PARAMETER_SPEC = new PSSParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, 32, 1);

    private static final Logger LOGGER = LoggerFactory.getLogger(IbanityHttpSignatureServiceImpl.class);
    private static final String SIGNATURE_HEADER_TEMPLATE = "keyId=\"%s\",created=\"%s\",algorithm=\"%s\",headers=\"%s\",signature=\"%s\"";
    private static final String DIGEST_ALGORITHM = MessageDigestAlgorithms.SHA_512;
    private static final Charset UTF8_CHARSET = StandardCharsets.UTF_8;
    private static final String ACCEPTED_HEADERS_REGEX = "(authorization|ibanity.*?)";
    private static final Pattern HEADERS_PATTERN = Pattern.compile(ACCEPTED_HEADERS_REGEX, Pattern.CASE_INSENSITIVE);
    private static final String SIGNATURE_HEADER_ALGORITHM = "hs2019";
    private Clock clock;
    private String certificateId;
    private PrivateKey privateKey;
    private String ibanityEndpoint;
    private String proxyEndpoint;

    public IbanityHttpSignatureServiceImpl(
            @NonNull PrivateKey privateKey,
            @NonNull X509Certificate certificate,
            @NonNull String certificateId,
            @NonNull String ibanityEndpoint) {
        this(privateKey, certificate, certificateId, Clock.systemUTC(), ibanityEndpoint, null);
    }

    public IbanityHttpSignatureServiceImpl(
            @NonNull PrivateKey privateKey,
            @NonNull X509Certificate certificate,
            @NonNull String certificateId,
            @NonNull String ibanityEndpoint,
            @NonNull String proxyEndpoint) {
        this(privateKey, certificate, certificateId, Clock.systemUTC(), ibanityEndpoint, proxyEndpoint);
    }

    public IbanityHttpSignatureServiceImpl(
            @NonNull PrivateKey privateKey,
            @NonNull X509Certificate certificate,
            @NonNull String certificateId,
            @NonNull Clock clock,
            @NonNull String ibanityEndpoint) {
        this.privateKey = privateKey;
        this.certificateId = certificateId;
        this.clock = clock;
        this.ibanityEndpoint = ibanityEndpoint;
    }

    public IbanityHttpSignatureServiceImpl(
            @NonNull PrivateKey privateKey,
            @NonNull X509Certificate certificate,
            @NonNull String certificateId,
            @NonNull Clock clock,
            @NonNull String ibanityEndpoint,
            String proxyEndpoint) {
        this.privateKey = privateKey;
        this.certificateId = certificateId;
        this.clock = clock;
        this.ibanityEndpoint = ibanityEndpoint;
        this.proxyEndpoint = proxyEndpoint;
    }

    @Override
    public Map<String, String> getHttpSignatureHeaders(
            @NonNull String httpMethod,
            @NonNull URL url,
            @NonNull Map<String, String> requestHeaders) {
        return getHttpSignatureHeaders(httpMethod, url, requestHeaders, "");
    }

    @Override
    public Map<String, String> getHttpSignatureHeaders(
            @NonNull String httpMethod,
            @NonNull URL url,
            @NonNull Map<String, String> requestHeaders,
            String payload) {
        return getHttpSignatureHeaders(httpMethod, url, requestHeaders, IOUtils.toInputStream(payload, UTF8_CHARSET));
    }

    @Override
    public Map<String, String> getHttpSignatureHeaders(
            @NonNull String httpMethod,
            @NonNull URL url,
            @NonNull Map<String, String> requestHeaders,
            InputStream inputStream) {
        return httpSignatureHeaders(httpMethod, url, requestHeaders, getDigestHeader(inputStream));
    }

    private Map<String, String> httpSignatureHeaders(
            @NonNull String httpMethod,
            @NonNull URL url,
            @NonNull Map<String, String> requestHeaders,
            @NonNull String payloadDigestHeaderValue) {
        Map<String, String> httpSignatureHeaders = new HashMap<>();

        Long createdTimestamp = getTimestamp();
        String signatureDigest = getSignatureDigest(getRequestTarget(httpMethod, url), getHost(), payloadDigestHeaderValue, createdTimestamp, requestHeaders);
        String signatureHeaderValue = getSignatureHeader(certificateId, createdTimestamp, SIGNATURE_HEADER_ALGORITHM, getSignatureHeaders(requestHeaders), signatureDigest);

        httpSignatureHeaders.put("Digest", payloadDigestHeaderValue);
        httpSignatureHeaders.put("Signature", signatureHeaderValue);

        return httpSignatureHeaders;
    }

    private String getHost() {
        try {
            return new URL(ibanityEndpoint).getHost();
        } catch (MalformedURLException e) {
            throw new RuntimeException("Ibanity api endpoint misconfigured: " + ibanityEndpoint, e);
        }
    }

    public static String getDigestHeader(InputStream payload) {
        MessageDigest md = getDigest();
        try (BufferedInputStream stream = new BufferedInputStream(payload, BUFFER_SIZE)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int length;
            while((length = stream.read(buffer)) != -1) {
                md.update(buffer, 0, length);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not read payload");
        }

        String digest = Base64.getEncoder().encodeToString(md.digest());
        return DIGEST_ALGORITHM + "=" + digest;
    }

    private static MessageDigest getDigest() {
        try {
            return MessageDigest.getInstance(DIGEST_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("Unsupported digest algorithm:" + DIGEST_ALGORITHM);
        }
    }

    private Long getTimestamp() {
        return Instant.now(clock).getEpochSecond();
    }

    private String getSignatureHeader(String certificateId, Long created, String algorithm, String headers, String signature) {
        return String.format(SIGNATURE_HEADER_TEMPLATE, certificateId, created, algorithm, headers, signature);
    }

    private String getSignatureHeaders(Map<String, String> requestHeaders) {
        List<String> headers = new ArrayList<>(Arrays.asList("(request-target)", "host", "digest", "(created)"));
        List<String> additionalHeaders = getAdditionalHeaders(requestHeaders).keySet().stream()
                .map(String::toLowerCase)
                .collect(toList());
        headers.addAll(additionalHeaders);
        return String.join(" ", headers);
    }

    private Map<String, String> getAdditionalHeaders(Map<String, String> requestHeaders) {
        return requestHeaders.entrySet().stream()
                .filter(entry -> HEADERS_PATTERN.matcher(entry.getKey()).matches())
                .collect(Collectors.toMap(
                        entry -> entry.getKey().toLowerCase(),
                        Map.Entry::getValue));
    }

    private String getSignatureDigest(String requestTarget, String host, String payloadDigest, Long timestamp, Map<String, String> requestHeaders) {
        try {
            String signatureString = getSignatureString(requestTarget, host, payloadDigest, timestamp, requestHeaders);
            if(LOGGER.isTraceEnabled()) {
                LOGGER.trace("Signature value: {}", signatureString);
            }

            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.setParameter(PARAMETER_SPEC);
            signature.initSign(privateKey);
            signature.update(signatureString.getBytes());
            byte[] signedData = signature.sign();

            return Base64.getEncoder().encodeToString(signedData);
        } catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException | InvalidAlgorithmParameterException exception) {
            String errorMessage = "Error while trying to generate the signature of the request";
            throw new IllegalArgumentException(errorMessage, exception);
        }
    }

    private String getSignatureString(String requestTarget, String host, String payloadDigest, Long timestamp, Map<String, String> requestHeaders) {
        List<String> values = new ArrayList<>(Arrays.asList(
                "(request-target): " + requestTarget,
                "host: " + host,
                "digest: " + payloadDigest,
                "(created): " + timestamp));

        values.addAll(getAdditionalHeaders(requestHeaders).entrySet().stream()
                .map(entry -> String.format("%s: %s", entry.getKey().toLowerCase(), entry.getValue()))
                .collect(Collectors.toList()));

        return String.join("\n", values);
    }

    private String getRequestTarget(String httpMethod, URL url) {
        String requestTarget = httpMethod.toLowerCase() + " " + getPath(url);
        if (url.getQuery() != null) {
            requestTarget += "?" + url.getQuery();
        }
        return requestTarget;
    }

    private String getPath(URL url) {
        if(proxyEndpoint == null) {
            return url.getPath();
        } else {
            String path = url.getPath();
            return path.replace(URI.create(proxyEndpoint).getPath(), "");
        }
    }
}
