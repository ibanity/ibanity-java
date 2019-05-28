package com.ibanity.apis.client.products.xs2a.models.factory.read;

import com.ibanity.apis.client.paging.IbanityPagingSpec;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public final class AccountsReadQuery {
    private String customerAccessToken;
    private UUID financialInstitutionId;
    private UUID accountInformationAccessRequestId;
    private IbanityPagingSpec pagingSpec;
}
