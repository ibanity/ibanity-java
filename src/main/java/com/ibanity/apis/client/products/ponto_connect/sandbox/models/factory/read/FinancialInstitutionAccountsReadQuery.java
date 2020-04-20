package com.ibanity.apis.client.products.ponto_connect.sandbox.models.factory.read;

import com.ibanity.apis.client.paging.IbanityPagingSpec;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class FinancialInstitutionAccountsReadQuery {

    private String accessToken;

    private UUID financialInstitutionId;

    private IbanityPagingSpec pagingSpec;
}
