package com.ibanity.apis.client.products.ponto_connect.models.read;

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
public class OrganizationFinancialInstitutionReadQuery {

    private UUID financialInstitutionId;
    private String accessToken;

    @Builder.Default
    private Map<String, String> additionalHeaders = emptyMap();
}
