package com.ibanity.apis.client.products.ponto_connect.models.create;

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
public class SynchronizationCreateQuery {

    private String subtype;
    private String resourceId;
    private String resourceType;
    private String accessToken;

    @Builder.Default
    private Map<String, String> additionalHeaders = emptyMap();
}
