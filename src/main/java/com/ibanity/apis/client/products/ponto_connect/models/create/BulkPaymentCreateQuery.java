package com.ibanity.apis.client.products.ponto_connect.models.create;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class BulkPaymentCreateQuery {

    private String accessToken;
    private UUID accountId;

    private LocalDate requestedExecutionDate;
    private String reference;
    private String redirectUri;
    private boolean batchBookingPreferred;

    @Builder.Default
    private List<BulkPaymentCreateQuery.Payment> payments = emptyList();

    @Builder.Default
    private Map<String, String> additionalHeaders = emptyMap();

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Payment {

        private String remittanceInformationType;
        private String remittanceInformation;
        private String currency;
        private String creditorName;
        private String creditorAgentType;
        private String creditorAgent;
        private String creditorAccountReferenceType;
        private String creditorAccountReference;
        private BigDecimal amount;
        private String endToEndId;
    }
}
