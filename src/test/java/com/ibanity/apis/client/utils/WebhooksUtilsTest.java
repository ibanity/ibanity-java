package com.ibanity.apis.client.utils;

import com.ibanity.apis.client.models.IbanityWebhookEvent;
import com.ibanity.apis.client.webhooks.models.ponto_connect.SynchronizationSucceededWithoutChange;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

import static com.ibanity.apis.client.helpers.IbanityTestHelper.loadFile;
import static org.assertj.core.api.Assertions.assertThat;

class WebhooksUtilsTest {

    @Test
    void webhookEventParser_xs2a() throws IOException {
        String payload = loadFile("json/webhooks/xs2a/synchronizationSucceededWithoutChange.json");
        IbanityWebhookEvent ibanityWebhookEvent = WebhooksUtils.webhookEventParser(payload, "xs2a.synchronization.succeededWithoutChange");
        assertThat(ibanityWebhookEvent.getType()).isEqualTo(ibanityWebhookEvent.getType());
    }

    @Test
    void webhookEventParser_pontoConnect() throws IOException {
        String payload = loadFile("json/webhooks/ponto-connect/synchronizationSucceededWithoutChange.json");
        IbanityWebhookEvent ibanityWebhookEvent = WebhooksUtils.webhookEventParser(payload, "pontoConnect.synchronization.succeededWithoutChange");
        assertThat(ibanityWebhookEvent).isEqualTo(createExpectedWebhook()).usingRecursiveComparison();
    }

    private SynchronizationSucceededWithoutChange createExpectedWebhook() {
        return SynchronizationSucceededWithoutChange.builder()
                .type("pontoConnect.synchronization.succeededWithoutChange")
                .synchronizationSubtype("accountDetails")
                .accountId(UUID.fromString("12036924-f070-4832-8321-3fe18ba37480"))
                .synchronizationId(UUID.fromString("cabf4560-6e4a-4790-8af6-4f4df4bf441b"))
                .organizationId(UUID.fromString("429fd324-c238-4ee9-8420-01fa455cdcdc"))
                .id(UUID.fromString("93f515c8-2edb-4b71-8a70-89b535c3c3cd"))
                .createdAt(Instant.parse("2021-11-10T13:52:33.012Z"))
                .build();
    }
}
