package com.ibanity.apis.client.http.service.impl;

import com.google.common.collect.Maps;
import com.ibanity.apis.client.http.service.IbanityHttpSignatureService;
import lombok.NonNull;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.*;
import java.security.cert.X509Certificate;
import java.time.Clock;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;

public class IbanityHttpSignatureServiceImpl implements IbanityHttpSignatureService {

    private static final String SIGNATURE_HEADER_TEMPLATE = "keyId=\"%s\",algorithm=\"%s\",headers=\"%s\",signature=\"%s\"";
    private static final String DIGEST_ALGORITHM = MessageDigestAlgorithms.SHA_512;
    private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
    private static final String ACCEPTED_HEADERS_REGEX = "(authorization|ibanity.*?)";
    private static final Pattern HEADERS_PATTERN = Pattern.compile(ACCEPTED_HEADERS_REGEX, Pattern.CASE_INSENSITIVE);

    private Clock clock;
    private String certificateId;
    private PrivateKey privateKey;
    private X509Certificate certificate;
    private String ibanityEndpoint;

    public IbanityHttpSignatureServiceImpl(
            @NonNull PrivateKey privateKey,
            @NonNull X509Certificate certificate,
            @NonNull String certificateId,
            @NonNull String ibanityEndpoint) {
        this(privateKey, certificate, certificateId, Clock.systemUTC(), ibanityEndpoint);
    }

    public IbanityHttpSignatureServiceImpl(
            @NonNull PrivateKey privateKey,
            @NonNull X509Certificate certificate,
            @NonNull String certificateId,
            @NonNull Clock clock,
            @NonNull String ibanityEndpoint) {
        this.privateKey = privateKey;
        this.certificate = certificate;
        this.certificateId = certificateId;
        this.clock = clock;
        this.ibanityEndpoint = ibanityEndpoint;
    }


    @Override
    public Map<String, String> getHttpSignatureHeaders(
            @NonNull String httpMethod,
            @NonNull URL url,
            @NonNull Map<String, String> requestHeaders) {
        return getHttpSignatureHeaders(httpMethod, url, requestHeaders, null);
    }


    @Override
    public Map<String, String> getHttpSignatureHeaders(
            @NonNull String httpMethod,
            @NonNull URL url,
            @NonNull Map<String, String> requestHeaders,
            String payload) {
        HashMap<String, String> httpSignatureHeaders = Maps.newHashMap();

        String dateHeaderValue = getDateHeader();
        String payloadDigestHeaderValue = getDigestHeader(payload);
        String signatureDigest = getSignatureDigest(getRequestTarget(httpMethod, url), getHost(), payloadDigestHeaderValue, dateHeaderValue, requestHeaders);
        String signatureHeaderValue = getSignatureHeader(certificateId, getCertificateAlgorithm(certificate), getSignatureHeaders(requestHeaders), signatureDigest);

        httpSignatureHeaders.put("Date", dateHeaderValue);
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

    private String getDigestHeader(String payload) {
        try {
            if(payload == null) {
                payload = "";
            }

            String digest = Base64.getEncoder().encodeToString(MessageDigest.getInstance(DIGEST_ALGORITHM).digest(payload.getBytes(UTF8_CHARSET)));
            return DIGEST_ALGORITHM + "=" + digest;
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalArgumentException("Unsupported digest algorithm:" + DIGEST_ALGORITHM);
        }
    }

    private String getDateHeader() {
        return Instant.now(clock).toString();
    }

    private String getSignatureHeader(String certificateId, String algorithm, String headers, String signature) {
        return String.format(SIGNATURE_HEADER_TEMPLATE, certificateId, algorithm, headers, signature);
    }

    private String getSignatureHeaders(Map<String, String> requestHeaders) {
        List<String> headers = newArrayList("(request-target)", "host", "digest", "date");
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

    private String getSignatureDigest(String requestTarget, String host, String payloadDigest, String date, Map<String, String> requestHeaders) {
        try {
            String signatureString = getSignatureString(requestTarget, host, payloadDigest, date, requestHeaders);
            Signature signature = Signature.getInstance(certificate.getSigAlgName());
            signature.initSign(privateKey);
            signature.update(signatureString.getBytes());
            byte[] signedData = signature.sign();

            return Base64.getEncoder().encodeToString(signedData);
        } catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException exception) {
            String errorMessage = "Error while trying to generate the signature of the request";
            throw new IllegalArgumentException(errorMessage, exception);
        }
    }

    private String getSignatureString(String requestTarget, String host, String payloadDigest, String date, Map<String, String> requestHeaders) {
        List<String> values = newArrayList(
                "(request-target): " + requestTarget,
                "host: " + host,
                "digest: " + payloadDigest,
                "date: " + date);

        values.addAll(getAdditionalHeaders(requestHeaders).entrySet().stream()
                .map(entry -> String.format("%s: %s", entry.getKey().toLowerCase(), entry.getValue()))
                .collect(Collectors.toList()));

        return String.join("\n", values);
    }

    private String getCertificateAlgorithm(X509Certificate certificate) {
        String signatureAlgorithm = certificate.getSigAlgName();

        switch (signatureAlgorithm) {
            case "SHA256withRSA":
                return "rsa-sha256";
            case "SHA512withRSA":
                return "rsa-sha512";
            default:
                throw new IllegalArgumentException("Unsupported signature algorithm:" + signatureAlgorithm);
        }
    }

    private String getRequestTarget(String httpMethod, URL url) {
        String requestTarget = httpMethod.toLowerCase() + " " + url.getPath();
        if (url.getQuery() != null) {
            requestTarget += "?" + url.getQuery();
        }
        return requestTarget;
    }
}
