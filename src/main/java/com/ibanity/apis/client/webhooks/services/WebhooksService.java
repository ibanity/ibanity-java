package com.ibanity.apis.client.webhooks.services;

import com.ibanity.apis.client.models.IbanityWebhookEvent;

public interface WebhooksService {

    IbanityWebhookEvent verifyAndParseEvent(String payload, String jwt);

    void verify(String payload, String jwt);

    String keys();
}
