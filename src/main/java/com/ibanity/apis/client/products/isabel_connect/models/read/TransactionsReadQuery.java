package com.ibanity.apis.client.products.isabel_connect.models.read;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.Map;

import static java.util.Collections.emptyMap;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class TransactionsReadQuery {
    private String accessToken;
    private String accountId;
    private Instant from;
    private Instant to;
    private IsabelPagingSpec pagingSpec;

    @Builder.Default
    private Map<String, String> additionalHeaders = emptyMap();
}
