package com.ibanity.apis.client.products.xs2a.models.factory.create;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

import static java.util.Collections.emptyMap;

@Getter
@Builder
public final class CustomerAccessTokenCreationQuery {

    private String applicationCustomerReference;
    private UUID idempotencyKey;

    @Builder.Default
    private Map<String, String> additionalHeaders = emptyMap();
}
