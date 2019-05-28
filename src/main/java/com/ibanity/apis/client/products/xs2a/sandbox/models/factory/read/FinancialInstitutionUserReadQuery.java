package com.ibanity.apis.client.products.xs2a.sandbox.models.factory.read;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class FinancialInstitutionUserReadQuery {
    private UUID financialInstitutionUserId;
}
