package com.ibanity.apis.client.products.xs2a.models.factory.read;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public final class TransactionReadQuery {
    private String customerAccessToken;
    private UUID financialInstitutionId;
    private UUID accountId;
    private UUID transactionId;
}
