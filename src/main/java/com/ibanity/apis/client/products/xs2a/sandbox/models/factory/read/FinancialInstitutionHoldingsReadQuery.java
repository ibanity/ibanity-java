package com.ibanity.apis.client.products.xs2a.sandbox.models.factory.read;

import com.ibanity.apis.client.paging.IbanityPagingSpec;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class FinancialInstitutionHoldingsReadQuery {
    private UUID financialInstitutionId;
    private UUID financialInstitutionUserId;
    private UUID financialInstitutionAccountId;

    private IbanityPagingSpec pagingSpec;
}
