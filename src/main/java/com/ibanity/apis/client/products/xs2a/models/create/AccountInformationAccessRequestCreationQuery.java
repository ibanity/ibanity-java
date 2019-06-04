package com.ibanity.apis.client.products.xs2a.models.create;

import com.ibanity.apis.client.paging.IbanityPagingSpec;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;

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
    private List<String> requestedAccountReferences = emptyList();
    private IbanityPagingSpec pagingSpec;

    @Builder.Default
    private Map<String, String> additionalHeaders = emptyMap();
}
