package com.ibanity.apis.client.products.xs2a.models;

import com.ibanity.apis.client.models.IbanityModel;
import com.ibanity.apis.client.products.xs2a.models.links.BulkPaymentInitiationAuthorizationLinks;
import com.ibanity.apis.client.products.xs2a.models.links.FinancialInstitutionLinks;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BulkPaymentInitiationRequest implements IbanityModel {

    public static final String RESOURCE_TYPE    = "bulkPaymentInitiationRequest";
    public static final String RESOURCE_PATH    = "bulk-payment-initiation-requests";
    public static final String API_URL_TAG_ID   = "{paymentInitiationRequest" + URL_PARAMETER_ID_POSTFIX + "}";

    private UUID id;
    private UUID financialInstitutionId;
    private String requestId;

    private String selfLink;
    private String consentReference;
    private String productType;
    private String debtorName;
    private String debtorAccountReference;
    private String debtorAccountReferenceType;
    private String status;
    private String statusReason;
    private String financialInstitutionCustomerReference;
    private boolean allowFinancialInstitutionRedirectUri;
    private boolean skipIbanityCompletionCallback;

    @Builder.Default
    private List<Payment> payments = Collections.emptyList();

    private LocalDate requestedExecutionDate;

    private BulkPaymentInitiationAuthorizationLinks links;
    private FinancialInstitutionLinks financialInstitutionLink;

    @Data
    @Builder
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
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
