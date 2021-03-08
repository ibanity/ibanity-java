package com.ibanity.apis.client.products.isabel_connect.models.read;

import lombok.*;

import java.util.Map;
import java.util.UUID;

import static com.ibanity.apis.client.models.IbanityModel.URL_PARAMETER_ID_POSTFIX;
import static java.util.Collections.emptyMap;

@Data
@Getter
@Builder
@ToString
@EqualsAndHashCode
public final class AccountReadQuery {

    private String accessToken;
    private String accountId;

    @Builder.Default
    private Map<String, String> additionalHeaders = emptyMap();
}