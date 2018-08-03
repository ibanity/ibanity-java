package com.ibanity.apis.client.models.factory.read;

import com.ibanity.apis.client.paging.IbanityPagingSpec;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public final class FinancialInstitutionsReadQuery {
    private String customerAccessToken;
    private IbanityPagingSpec pagingSpec;
}
