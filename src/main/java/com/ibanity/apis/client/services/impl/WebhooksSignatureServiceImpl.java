package com.ibanity.apis.client.services.impl;

import com.ibanity.apis.client.exceptions.IbanityRuntimeException;
import com.ibanity.apis.client.services.WebhooksSignatureService;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtContext;

import java.io.IOException;

import static com.ibanity.apis.client.mappers.ModelMapperHelper.readResponseContent;
import static com.ibanity.apis.client.utils.WebhooksUtils.getDigest;

public class WebhooksSignatureServiceImpl implements WebhooksSignatureService {

    private final JwtConsumer jwtConsumer;

    public WebhooksSignatureServiceImpl(JwtConsumer jwtConsumer) {
        this.jwtConsumer = jwtConsumer;
    }

    @Override
    public void verify(HttpResponse httpResponse) {
        try {
            verify(readResponseContent(httpResponse.getEntity()), httpResponse.getFirstHeader("signature"));
        } catch (IOException e) {
            throw new IbanityRuntimeException("Invalid response body.", e);
        }
    }

    @Override
    public void verify(String payload, Header signatureHeader) {
        verify(payload, signatureHeader.getValue());
    }

    @Override
    public void verify(String payload, String jwt) {
        try {
            JwtContext jwtContext = jwtConsumer.process(jwt);
            String digest = getDigest(payload);
            if (!digest.equals(jwtContext.getJwtClaims().getStringClaimValue("digest"))) {
                throw new IbanityRuntimeException("Signature digest value mismatch.");
            }
        } catch (InvalidJwtException | MalformedClaimException e) {
            throw new IbanityRuntimeException(e.getMessage(), e);
        }
    }
}
