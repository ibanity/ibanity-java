package com.ibanity.apis.client.services;

import com.ibanity.apis.client.models.webhooks.IbanityWebhooks;

public interface WebhooksService {

    IbanityWebhooks verifyAndParseEvent(String payload, String jwt);

    void verify(String payload, String jwt);

    String keys();
}
