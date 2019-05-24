package com.ibanity.apis.client.models.factory.create;

import com.ibanity.apis.client.paging.IbanityPagingSpec;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public final class AccountInformationAccessRequestCreationQuery {
    private UUID accountInformationAccessRequestId;
    private UUID financialInstitutionId;
    private UUID idempotencyKey;
    private String consentReference;
    private String redirectURI;
    private String customerAccessToken;
    private String locale;
    private String customerIpAddress;

    @Singular
    private List<String> requestedAccountReferences;
    private IbanityPagingSpec pagingSpec;
}
