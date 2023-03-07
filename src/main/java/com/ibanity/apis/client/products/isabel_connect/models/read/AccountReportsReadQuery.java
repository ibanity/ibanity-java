package com.ibanity.apis.client.products.isabel_connect.models.read;

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
public class AccountReportsReadQuery {

    private String accessToken;
    private String after;
    private IsabelPagingSpec pagingSpec;

    @Builder.Default
    private Map<String, String> additionalHeaders = emptyMap();
}
