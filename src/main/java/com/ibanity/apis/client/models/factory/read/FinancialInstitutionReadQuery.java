package com.ibanity.apis.client.models.factory.read;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public final class FinancialInstitutionReadQuery {
    private String customerAccessToken;
    private UUID financialInstitutionId;
}
