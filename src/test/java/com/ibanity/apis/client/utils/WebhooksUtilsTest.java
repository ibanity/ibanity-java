package com.ibanity.apis.client.utils;

import com.ibanity.apis.client.models.IbanityWebhookEvent;
import org.junit.jupiter.api.Test;

import java.io.IOException;

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
        assertThat(ibanityWebhookEvent.getType()).isEqualTo(com.ibanity.apis.client.webhooks.models.ponto_connect.SynchronizationSucceededWithoutChange.TYPE);
    }
}
