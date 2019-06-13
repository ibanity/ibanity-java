package com.ibanity.apis.client.products.xs2a.models.delete;

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
public class CustomerDeleteQuery {

    private String customerAccessToken;

    @Builder.Default
    private Map<String, String> additionalHeaders = emptyMap();
}
