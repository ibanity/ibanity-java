package com.ibanity.apis.client.sandbox.models.factory.read;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class FinancialInstitutionReadQuery {
    private UUID financialInstitutionId;
}
