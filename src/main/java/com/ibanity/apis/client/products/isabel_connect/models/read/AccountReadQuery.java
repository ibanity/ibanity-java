package com.ibanity.apis.client.products.isabel_connect.models.read;

import lombok.*;

import java.util.Map;

import static java.util.Collections.emptyMap;

@Data
@Getter
@Builder
@ToString
@EqualsAndHashCode
public class AccountReadQuery {

    private String accessToken;
    private String accountId;

    @Builder.Default
    private Map<String, String> additionalHeaders = emptyMap();
}