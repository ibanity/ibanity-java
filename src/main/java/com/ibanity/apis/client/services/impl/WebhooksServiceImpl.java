package com.ibanity.apis.client.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.ibanity.apis.client.exceptions.IbanityRuntimeException;
import com.ibanity.apis.client.models.webhooks.IbanityWebhooks;
import com.ibanity.apis.client.models.webhooks.xs2a.*;
import com.ibanity.apis.client.services.ApiUrlProvider;
import com.ibanity.apis.client.services.WebhooksService;
import com.ibanity.apis.client.utils.IbanityUtils;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtContext;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.mapWebhookResource;
import static com.ibanity.apis.client.utils.WebhooksUtils.getDigest;

public class WebhooksServiceImpl implements WebhooksService {

    private final JwtConsumer jwtConsumer;
    private final ApiUrlProvider apiUrlProvider;

    public WebhooksServiceImpl(ApiUrlProvider apiUrlProvider, JwtConsumer jwtConsumer) {
        this.jwtConsumer = jwtConsumer;
        this.apiUrlProvider = apiUrlProvider;
    }

    @Override
    public IbanityWebhooks verifyAndParseEvent(String payload, String jwt) {
        verify(payload, jwt);

        try {
            JsonNode jsonNode = IbanityUtils.objectMapper().readTree(payload);
            String type = jsonNode.get("data").get("type").textValue();
            IbanityWebhooks ibanityWebhooks = null;
            switch (type) {
                case "xs2a.account.detailsUpdated":
                    ibanityWebhooks = mapWebhookResource(payload, AccountDetailsUpdated.mappingFunction());
                    break;
                case "xs2a.account.transactionsCreated":
                    ibanityWebhooks = mapWebhookResource(payload, AccountTransactionsCreated.mappingFunction());
                    break;
                case "xs2a.account.transactionsUpdated":
                    ibanityWebhooks = mapWebhookResource(payload, AccountTransactionsUpdated.mappingFunction());
                    break;
                case "xs2a.synchronization.failed":
                    ibanityWebhooks = mapWebhookResource(payload, SynchronizationFailed.mappingFunction());
                    break;
                case "xs2a.synchronization.succeededWithoutChange":
                    ibanityWebhooks = mapWebhookResource(payload, SynchronizationSucceededWithoutChange.mappingFunction());
                    break;
            }

            return ibanityWebhooks;
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
}
