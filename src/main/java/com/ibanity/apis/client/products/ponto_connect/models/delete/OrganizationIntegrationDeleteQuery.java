package com.ibanity.apis.client.products.ponto_connect.models.delete;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;
import java.util.UUID;

import static java.util.Collections.emptyMap;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class OrganizationIntegrationDeleteQuery {

    private String accessToken;
    private UUID organizationId;

    @Builder.Default
    private Map<String, String> additionalHeaders = emptyMap();
}
