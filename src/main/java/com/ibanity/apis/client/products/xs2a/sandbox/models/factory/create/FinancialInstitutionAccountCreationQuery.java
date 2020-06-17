package com.ibanity.apis.client.products.xs2a.sandbox.models.factory.create;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class FinancialInstitutionAccountCreationQuery {
    private UUID financialInstitutionId;
    private UUID financialInstitutionUserId;

    private String subtype;
    private String currency;
    private String description;
    private String reference;
    private String referenceType;
    private BigDecimal currentBalance;
    private BigDecimal availableBalance;

    private String product;
    private String holderName;
    private Instant currentBalanceChangedAt;
    private Instant currentBalanceReferenceDate;
    private Instant availableBalanceChangedAt;
    private Instant availableBalanceReferenceDate;
}
