package com.ibanity.apis.client.products.xs2a.models.create;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

import static java.util.Collections.emptyMap;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public final class CustomerAccessTokenCreationQuery {

    private String applicationCustomerReference;

    @Builder.Default
    private Map<String, String> additionalHeaders = emptyMap();
}
