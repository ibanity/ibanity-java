package com.ibanity.apis.client.models.factory.create;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public final class AccountInformationAccessRequestCreationQuery {
    private String customerAccessToken;
    private UUID financialInstitutionId;
    private String redirectURI;
    private String consentReference;
    private UUID idempotencyKey;
    private List<String> requestedAccountReference;
}
