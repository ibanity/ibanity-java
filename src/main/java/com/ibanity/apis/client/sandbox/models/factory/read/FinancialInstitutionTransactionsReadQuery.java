package com.ibanity.apis.client.sandbox.models.factory.read;

import com.ibanity.apis.client.paging.IbanityPagingSpec;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class FinancialInstitutionTransactionsReadQuery {
    private UUID financialInstitutionId;
    private UUID financialInstitutionUserId;
    private UUID financialInstitutionAccountId;

    private IbanityPagingSpec pagingSpec;

    private UUID idempotencyKey;
}
