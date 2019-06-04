package com.ibanity.apis.client.products.xs2a.models.read;

import com.ibanity.apis.client.paging.IbanityPagingSpec;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

import static java.util.Collections.emptyMap;

@Getter
@Builder
public final class FinancialInstitutionsReadQuery {

    private String customerAccessToken;
    private IbanityPagingSpec pagingSpec;

    @Builder.Default
    private Map<String, String> additionalHeaders = emptyMap();
}
