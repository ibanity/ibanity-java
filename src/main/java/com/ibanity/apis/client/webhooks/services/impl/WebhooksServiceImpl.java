package com.ibanity.apis.client.webhooks.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.ibanity.apis.client.exceptions.IbanityRuntimeException;
import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.IbanityWebhookEvent;
import com.ibanity.apis.client.services.ApiUrlProvider;
import com.ibanity.apis.client.utils.IbanityUtils;
import com.ibanity.apis.client.utils.WebhooksUtils;
import com.ibanity.apis.client.webhooks.services.WebhooksService;
import org.apache.http.HttpResponse;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtContext;

import java.io.IOException;

import static com.ibanity.apis.client.mappers.ModelMapperHelper.readResponseContent;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;
import static com.ibanity.apis.client.utils.WebhooksUtils.getDigest;

public class WebhooksServiceImpl implements WebhooksService {

    private final JwtConsumer jwtConsumer;
    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public WebhooksServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient, JwtConsumer jwtConsumer) {
        this.jwtConsumer = jwtConsumer;
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public IbanityWebhookEvent verifyAndParseEvent(String payload, String jwt) {
        verify(payload, jwt);

        try {
            JsonNode jsonNode = IbanityUtils.objectMapper().readTree(payload);
            String type = jsonNode.get("data").get("type").textValue();
            return WebhooksUtils.webhookEventParser(payload, type);
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException("Response cannot be parsed", exception);
        }
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

    @Override
    public String keys() {
        HttpResponse httpResponse = ibanityHttpClient.get(buildUri(apiUrlProvider.find("webhooks", "keys")));
        try {
            return readResponseContent(httpResponse.getEntity());
        } catch (IOException e) {
            throw new IllegalArgumentException("Response cannot be parsed", e);
        }
    }
}
