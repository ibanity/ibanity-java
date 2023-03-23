package com.ibanity.apis.client.products.ponto_connect.models.read;

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
public class IntegrationAccountsReadQuery {

    private String accessToken;

    private IbanityPagingSpec pagingSpec;

    @Builder.Default
    private Map<String, String> additionalHeaders = emptyMap();

}
