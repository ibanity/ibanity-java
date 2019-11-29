package com.ibanity.apis.client.products.xs2a.models.create;

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
public class AuthorizationCreationQuery {

    private UUID financialInstitutionId;
    private UUID accountInformationAccessRequestId;
    private String customerAccessToken;
    private String credentialsEncryptionKey;

    @Builder.Default
    private Map<String, String> queryParameters = emptyMap();

    @Builder.Default
    private Map<String, String> additionalHeaders = emptyMap();

}
