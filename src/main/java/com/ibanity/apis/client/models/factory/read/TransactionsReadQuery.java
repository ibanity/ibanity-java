package com.ibanity.apis.client.models.factory.read;

import com.ibanity.apis.client.paging.IbanityPagingSpec;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public final class TransactionsReadQuery {
    private String customerAccessToken;
    private UUID financialInstitutionId;
    private UUID accountId;
    private IbanityPagingSpec pagingSpec;
}
