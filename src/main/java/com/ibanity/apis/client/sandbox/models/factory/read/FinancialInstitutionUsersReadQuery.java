package com.ibanity.apis.client.sandbox.models.factory.read;

import com.ibanity.apis.client.paging.IbanityPagingSpec;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FinancialInstitutionUsersReadQuery {
    private IbanityPagingSpec pagingSpec;
}
