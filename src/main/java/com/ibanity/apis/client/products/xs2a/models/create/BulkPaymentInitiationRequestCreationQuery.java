package com.ibanity.apis.client.products.xs2a.models.create;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

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
public final class BulkPaymentInitiationRequestCreationQuery {

    private String customerAccessToken;
    private UUID financialInstitutionId;

    private String consentReference;
    private String productType;

    private String debtorName;
    private String debtorAccountReference;
    private String debtorAccountReferenceType;

    private String redirectUri;
    private String locale;
    private String customerIpAddress;

    private boolean allowFinancialInstitutionRedirectUri;
    private boolean skipIbanityCompletionCallback;
    private String state;
    private String financialInstitutionCustomerReference;

    private LocalDate requestedExecutionDate;

    @Builder.Default
    private Map<String, String> additionalHeaders = emptyMap();

    @Builder.Default
    private List<Payment> payments = emptyList();

    @Getter
    @Builder
    @ToString
    @EqualsAndHashCode
    public static class Payment {
        private String endToEndId;

        private String remittanceInformationType;
        private String remittanceInformation;

        private BigDecimal amount;
        private String currency;

        private String creditorName;
        private String creditorAccountReference;
        private String creditorAccountReferenceType;
        private String creditorAgent;
        private String creditorAgentType;
    }
}
