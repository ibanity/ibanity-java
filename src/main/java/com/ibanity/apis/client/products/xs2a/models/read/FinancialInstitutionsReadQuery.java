package com.ibanity.apis.client.products.xs2a.models.read;

import com.ibanity.apis.client.paging.IbanityOffsetPagingSpec;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.products.ponto_connect.models.Filter;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.emptyMap;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public final class FinancialInstitutionsReadQuery {

    private String customerAccessToken;
    private IbanityPagingSpec pagingSpec;
    private IbanityOffsetPagingSpec offsetPagingSpec;

    @Builder.Default
    private Map<String, String> additionalHeaders = emptyMap();

    @Builder.Default
    private List<Filter> filters = newArrayList();
}
