package com.ibanity.apis.client.models;

import com.ibanity.apis.client.models.links.FinancialInstitutionLinks;
import com.ibanity.apis.client.models.links.PaymentAccessLinks;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInitiationRequest implements IbanityModel {

    public static final String RESOURCE_TYPE    = "paymentInitiationRequest";
    public static final String RESOURCE_PATH    = "payment-initiation-requests";
    public static final String API_URL_TAG_ID   = "{" + RESOURCE_TYPE + URL_PARAMETER_ID_POSTFIX + "}";

    private UUID id;
    private UUID financialInstitutionId;

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
    private String redirectUri;
    private String locale;
    private String customerIpAddress;

    private Double amount;

    private LocalDate requestedExecutionDate;

    private PaymentAccessLinks links;
    private FinancialInstitutionLinks financialInstitutionLink;
}
