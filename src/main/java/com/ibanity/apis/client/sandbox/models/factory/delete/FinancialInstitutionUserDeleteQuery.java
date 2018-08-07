package com.ibanity.apis.client.sandbox.models.factory.delete;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class FinancialInstitutionUserDeleteQuery {
    private UUID financialInstitutionUserId;

    private UUID idempotencyKey;
}
