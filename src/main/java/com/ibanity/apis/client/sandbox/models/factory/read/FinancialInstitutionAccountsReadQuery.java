package com.ibanity.apis.client.sandbox.models.factory.read;

import com.ibanity.apis.client.paging.IbanityPagingSpec;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class FinancialInstitutionAccountsReadQuery {
    private UUID financialInstitutionId;
    private UUID financialInstitutionUserId;

    private IbanityPagingSpec pagingSpec;
}
