package com.ibanity.apis.client.utils;

import org.apache.commons.codec.digest.MessageDigestAlgorithms;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class WebhooksUtils {

    private static final String DIGEST_ALGORITHM = MessageDigestAlgorithms.SHA_512;
    private static final int BUFFER_SIZE = 32_768;

    private static MessageDigest getMessageDigest() {
        try {
            return MessageDigest.getInstance(DIGEST_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("Unsupported digest algorithm:" + DIGEST_ALGORITHM);
        }
    }

    public static String getDigest(String payload) {
        MessageDigest md = getMessageDigest();
        try (BufferedInputStream stream = new BufferedInputStream(new ByteArrayInputStream(payload.getBytes()))) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int length;
            while ((length = stream.read(buffer)) != -1) {
                md.update(buffer, 0, length);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not read payload.");
        }

        return Base64.getEncoder().encodeToString(md.digest());
    }
}
