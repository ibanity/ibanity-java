package com.ibanity.apis.client.products.xs2a.models.create;

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
public final class PaymentInitiationRequestCreationQuery {

    private String customerAccessToken;
    private UUID financialInstitutionId;

    private String consentReference;
    private String endToEndId;
    private String productType;

    private String remittanceInformationType;
    private String remittanceInformation;

    private BigDecimal amount;
    private String currency;

    private String debtorName;
    private String debtorAccountReference;
    private String debtorAccountReferenceType;

    private String creditorName;
    private String creditorAccountReference;
    private String creditorAccountReferenceType;
    private String creditorAgent;
    private String creditorAgentType;

    private String redirectUri;
    private String locale;
    private String customerIpAddress;

    private boolean allowFinancialInstitutionRedirectUri;
    private boolean skipIbanityCompletionCallback;
    private String state;

    @Builder.Default
    private Map<String, String> additionalHeaders = emptyMap();
}
