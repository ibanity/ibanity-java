package com.ibanity.apis.client.products.ponto_connect.sandbox.models.factory.update;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class FinancialInstitutionTransactionUpdateQuery {
    private String accessToken;
    private UUID financialInstitutionId;
    private UUID financialInstitutionAccountId;
    private UUID financialInstitutionTransactionId;

    private String counterpartName;
    private String description;
    private String remittanceInformation;
    private String bankTransactionCode;
    private String proprietaryBankTransactionCode;
    private String additionalInformation;
    private String creditorId;
    private String mandateId;
    private String purposeCode;
    private String endToEndId;
}
