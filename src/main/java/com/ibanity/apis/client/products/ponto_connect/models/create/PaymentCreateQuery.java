package com.ibanity.apis.client.products.ponto_connect.models.create;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import static java.util.Collections.emptyMap;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class PaymentCreateQuery {

    private String accessToken;
    private UUID accountId;

    @Builder.Default
    private Map<String, String> additionalHeaders = emptyMap();

    private LocalDate requestedExecutionDate;
    private String remittanceInformationType;
    private String remittanceInformation;
    private String currency;
    private BigDecimal amount;
    private String creditorName;
    private String creditorAgentType;
    private String creditorAgent;
    private String creditorAccountReferenceType;
    private String creditorAccountReference;
    private String redirectUri;
    private String endToEndId;
}
