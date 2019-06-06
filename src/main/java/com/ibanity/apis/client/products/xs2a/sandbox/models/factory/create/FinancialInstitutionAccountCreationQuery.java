package com.ibanity.apis.client.products.xs2a.sandbox.models.factory.create;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class FinancialInstitutionAccountCreationQuery {
    private UUID financialInstitutionId;
    private UUID financialInstitutionUserId;

    private String subType;
    private String currency;
    private String description;
    private String reference;
    private String referenceType;
    private Double currentBalance;
    private Double availableBalance;

}
