package com.ibanity.apis.client.webhooks.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.ibanity.apis.client.exceptions.IbanityRuntimeException;
import com.ibanity.apis.client.models.IbanityWebhookEvent;
import com.ibanity.apis.client.services.ApiUrlProvider;
import com.ibanity.apis.client.utils.IbanityUtils;
import com.ibanity.apis.client.webhooks.models.xs2a.*;
import com.ibanity.apis.client.webhooks.services.WebhooksService;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtContext;

import static com.ibanity.apis.client.mappers.IbanityWebhookEventMapper.mapWebhookResource;
import static com.ibanity.apis.client.utils.WebhooksUtils.getDigest;

public class WebhooksServiceImpl implements WebhooksService {

    private final JwtConsumer jwtConsumer;
    private final ApiUrlProvider apiUrlProvider;

    public WebhooksServiceImpl(ApiUrlProvider apiUrlProvider, JwtConsumer jwtConsumer) {
        this.jwtConsumer = jwtConsumer;
        this.apiUrlProvider = apiUrlProvider;
    }

    @Override
    public IbanityWebhookEvent verifyAndParseEvent(String payload, String jwt) {
        verify(payload, jwt);

        try {
            JsonNode jsonNode = IbanityUtils.objectMapper().readTree(payload);
            String type = jsonNode.get("data").get("type").textValue();
            return webhookEventParser(payload, type);
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
        return apiUrlProvider.find("webhooks", "keys");
    }

    private IbanityWebhookEvent webhookEventParser(String payload, String type) {
        IbanityWebhookEvent ibanityWebhookEvent = null;
        switch (type) {
            case "xs2a.account.detailsUpdated":
                ibanityWebhookEvent = mapWebhookResource(payload, AccountDetailsUpdated.mappingFunction());
                break;
            case "xs2a.account.transactionsCreated":
                ibanityWebhookEvent = mapWebhookResource(payload, AccountTransactionsCreated.mappingFunction());
                break;
            case "xs2a.account.transactionsUpdated":
                ibanityWebhookEvent = mapWebhookResource(payload, AccountTransactionsUpdated.mappingFunction());
                break;
            case "xs2a.synchronization.failed":
                ibanityWebhookEvent = mapWebhookResource(payload, SynchronizationFailed.mappingFunction());
                break;
            case "xs2a.synchronization.succeededWithoutChange":
                ibanityWebhookEvent = mapWebhookResource(payload, SynchronizationSucceededWithoutChange.mappingFunction());
                break;
        }

        return ibanityWebhookEvent;
    }
}
