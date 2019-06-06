package com.ibanity.apis.client.products.xs2a.sandbox.models.factory.create;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Builder
@ToString
@EqualsAndHashCode
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
