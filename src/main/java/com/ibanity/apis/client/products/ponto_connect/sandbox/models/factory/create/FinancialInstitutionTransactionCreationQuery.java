package com.ibanity.apis.client.products.ponto_connect.sandbox.models.factory.create;

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

    private String accessToken;
    private UUID financialInstitutionId;
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
    private String additionalInformation;
    private String creditorId;
    private String mandateId;
    private String purposeCode;
    private String endToEndId;
    private BigDecimal fee;
    private String cardReference;
    private String cardReferenceType;

    private boolean automaticBooking;
}