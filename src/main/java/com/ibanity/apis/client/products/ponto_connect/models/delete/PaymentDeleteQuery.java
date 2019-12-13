package com.ibanity.apis.client.products.ponto_connect.models.delete;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;
import java.util.UUID;

import static java.util.Collections.emptyMap;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class PaymentDeleteQuery {

    private String accessToken;
    private UUID accountId;
    private UUID paymentId;

    @Builder.Default
    private Map<String, String> additionalHeaders = emptyMap();
}
