package com.ibanity.apis.client.products.xs2a.models.create;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

import static java.util.Collections.emptyMap;

@Getter
@Builder
public final class CustomerAccessTokenCreationQuery {

    private String applicationCustomerReference;

    @Builder.Default
    private Map<String, String> additionalHeaders = emptyMap();
}
