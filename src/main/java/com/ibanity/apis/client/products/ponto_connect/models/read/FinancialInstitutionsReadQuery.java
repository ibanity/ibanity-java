package com.ibanity.apis.client.products.ponto_connect.models.read;

import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.products.ponto_connect.models.Filter;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyMap;

import java.util.ArrayList;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class FinancialInstitutionsReadQuery {

    private IbanityPagingSpec pagingSpec;

    @Builder.Default
    private Map<String, String> additionalHeaders = emptyMap();

    @Builder.Default
    private List<Filter> filters = new ArrayList<>();
}
