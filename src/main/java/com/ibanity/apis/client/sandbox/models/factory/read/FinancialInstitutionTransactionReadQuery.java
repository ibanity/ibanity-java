package com.ibanity.apis.client.sandbox.models.factory.read;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class FinancialInstitutionTransactionReadQuery {
    private UUID financialInstitutionId;
    private UUID financialInstitutionUserId;
    private UUID financialInstitutionAccountId;
    private UUID financialInstitutionTransactionId;

    private UUID idempotencyKey;
}
