package com.ibanity.apis.client.models.factory.create;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public final class PaymentInitiationRequestCreationQuery {
    private String customerAccessToken;
    private UUID financialInstitutionId;
    private UUID idempotencyKey;

    private String consentReference;
    private String endToEndId;
    private String productType;

    private String remittanceInformationType;
    private String remittanceInformation;

    private Double amount;
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
}
