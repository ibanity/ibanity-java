package com.ibanity.apis.client.products.ponto_connect.models.create;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import static java.util.Collections.emptyMap;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class PaymentRequestCreateQuery {

    private String accessToken;
    private UUID accountId;

    @Builder.Default
    private Map<String, String> additionalHeaders = emptyMap();

    private String remittanceInformationType;
    private String remittanceInformation;
    private BigDecimal amount;
    private String redirectUri;
    private String endToEndId;
}
