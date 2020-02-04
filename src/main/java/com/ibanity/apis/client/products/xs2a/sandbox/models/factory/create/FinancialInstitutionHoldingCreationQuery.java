package com.ibanity.apis.client.products.xs2a.sandbox.models.factory.create;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class FinancialInstitutionHoldingCreationQuery {
    private UUID financialInstitutionId;
    private UUID financialInstitutionUserId;
    private UUID financialInstitutionAccountId;

    private String name;
    private String reference;
    private String referenceType;
    private String subtype;

    private BigDecimal quantity;

    private BigDecimal totalValuation;
    private String totalValuationCurrency;

    private String lastValuationCurrency;
    private BigDecimal lastValuation;
    private LocalDate lastValuationDate;
}
