package com.ibanity.apis.client.models;

import java.util.UUID;

public interface IbanityWebhookEvent {

    void setId(UUID t);
    void setType(String type);
    String getType();
}
