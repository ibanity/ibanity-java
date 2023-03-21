package com.ibanity.apis.client.products.ponto_connect.models;

import com.ibanity.apis.client.models.IbanityModel;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IntegrationAccount implements IbanityModel {

    public static final String RESOURCE_TYPE = "integrationAccount";
    public static final String API_URL_TAG_ID = "{" + RESOURCE_TYPE + URL_PARAMETER_ID_POSTFIX + "}";

    private UUID id;
    private UUID financialInstitutionId;
    private UUID accountId;
    private UUID organizationId;

    private String requestId;
    private String selfLink;

    private Instant createdAt;
    private Instant lastAccessedAt;

}
