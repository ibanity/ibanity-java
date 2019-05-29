package com.ibanity.apis.client.products.xs2a.models.factory.create;

import com.ibanity.apis.client.paging.IbanityPagingSpec;
import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
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

    @Builder.Default
    private List<String> requestedAccountReferences = Collections.emptyList();
    private IbanityPagingSpec pagingSpec;
}
