package com.ibanity.apis.client.products.xs2a.models;

import com.ibanity.apis.client.models.IbanityModel;
import com.ibanity.apis.client.products.xs2a.models.links.FinancialInstitutionLinks;
import com.ibanity.apis.client.products.xs2a.models.links.PaymentInitiationAuthorizationLinks;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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

    private BigDecimal amount;

    private LocalDate requestedExecutionDate;

    private PaymentInitiationAuthorizationLinks links;
    private FinancialInstitutionLinks financialInstitutionLink;
}
