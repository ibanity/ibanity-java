package com.ibanity.apis.client.products.ponto_connect.sandbox.models.factory.read;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class FinancialInstitutionAccountReadQuery {

    private String accessToken;

    private UUID financialInstitutionId;
    private UUID financialInstitutionAccountId;
}
