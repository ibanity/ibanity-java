package com.ibanity.apis.client.products.xs2a.sandbox.models.factory.create;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class FinancialInstitutionTransactionCreationQuery {
    private UUID financialInstitutionId;
    private UUID financialInstitutionUserId;
    private UUID financialInstitutionAccountId;

    private BigDecimal amount;
    private String currency;

    private Instant valueDate;
    private Instant executionDate;

    private String description;
    private String remittanceInformationType;
    private String remittanceInformation;
    private String counterpartName;
    private String counterpartReference;

    private String bankTransactionCode;
    private String proprietaryBankTransactionCode;
    private String endToEndId;
    private String purposeCode;
    private String mandateId;
    private String creditorId;
    private String additionalInformation;
    private BigDecimal fee;
    private String cardReference;
    private String cardReferenceType;
    private boolean automaticBooking;
}
