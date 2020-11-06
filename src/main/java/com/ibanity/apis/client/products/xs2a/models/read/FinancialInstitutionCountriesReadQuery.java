package com.ibanity.apis.client.products.xs2a.models.read;

import com.ibanity.apis.client.paging.IbanityPagingSpec;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

import static java.util.Collections.emptyMap;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public final class FinancialInstitutionCountriesReadQuery {

    @Builder.Default
    private Map<String, String> additionalHeaders = emptyMap();

    private IbanityPagingSpec pagingSpec;
}
