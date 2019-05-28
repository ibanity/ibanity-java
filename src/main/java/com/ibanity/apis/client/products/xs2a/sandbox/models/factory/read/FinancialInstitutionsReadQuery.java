package com.ibanity.apis.client.products.xs2a.sandbox.models.factory.read;

import com.ibanity.apis.client.paging.IbanityPagingSpec;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FinancialInstitutionsReadQuery {
    private IbanityPagingSpec pagingSpec;
}
