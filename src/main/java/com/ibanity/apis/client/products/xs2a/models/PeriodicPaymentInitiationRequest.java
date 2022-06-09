package com.ibanity.apis.client.products.xs2a.models;

import com.ibanity.apis.client.models.IbanityModel;
import com.ibanity.apis.client.products.xs2a.models.links.FinancialInstitutionLinks;
import com.ibanity.apis.client.products.xs2a.models.links.PeriodicPaymentInitiationAuthorizationLinks;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PeriodicPaymentInitiationRequest implements IbanityModel {

    public static final String RESOURCE_TYPE    = "periodicPaymentInitiationRequest";
    public static final String RESOURCE_PATH    = "periodic-payment-initiation-requests";
    public static final String API_URL_TAG_ID   = "{paymentInitiationRequest" + URL_PARAMETER_ID_POSTFIX + "}";

    private UUID id;
    private UUID financialInstitutionId;
    private String requestId;

    private String selfLink;
    private String consentReference;
    private String endToEndId;
    private String productType;
    private String remittanceInformationType;
    private String remittanceInformation;
    private String currency;
    private String debtorName;
    private String debtorAccountReference;
    private String debtorAccountReferenceType;
    private String creditorName;
    private String creditorAccountReference;
    private String creditorAccountReferenceType;
    private String creditorAgent;
    private String creditorAgentType;
    private String status;
    private String statusReason;
    private String financialInstitutionCustomerReference;
    private boolean allowFinancialInstitutionRedirectUri;
    private boolean skipIbanityCompletionCallback;

    private BigDecimal amount;

    private LocalDate startDate;
    private LocalDate endDate;
    private String frequency;
    private String executionRule;

    private PeriodicPaymentInitiationAuthorizationLinks links;
    private FinancialInstitutionLinks financialInstitutionLink;
}
