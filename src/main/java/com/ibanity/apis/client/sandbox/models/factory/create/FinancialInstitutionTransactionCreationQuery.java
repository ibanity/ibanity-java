package com.ibanity.apis.client.sandbox.models.factory.create;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class FinancialInstitutionTransactionCreationQuery {
    private UUID financialInstitutionId;
    private UUID financialInstitutionUserId;
    private UUID financialInstitutionAccountId;
    private UUID idempotencyKey;

    private Double amount;
    private String currency;

    private Instant valueDate;
    private Instant executionDate;

    private String description;
    private String remittanceInformationType;
    private String remittanceInformation;
    private String counterpartName;
    private String counterpartReference;

}
