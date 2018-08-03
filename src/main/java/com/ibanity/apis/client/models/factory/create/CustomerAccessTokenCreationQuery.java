package com.ibanity.apis.client.models.factory.create;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public final class CustomerAccessTokenCreationQuery {
    private String applicationCustomerReference;
    private UUID idempotencyKey;
}
