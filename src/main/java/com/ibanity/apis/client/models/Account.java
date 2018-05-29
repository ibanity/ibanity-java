package com.ibanity.apis.client.models;

import com.ibanity.apis.client.paging.PagingBehavior;
import io.crnk.core.resource.annotations.JsonApiResource;

import java.util.UUID;

@JsonApiResource(type = "account", resourcePath = "accounts", pagingBehavior = PagingBehavior.class)
public class Account extends AbstractAccount {

    public Account() {
        super();
    }

    public Account(UUID id, UUID financialInstitutionId) {
        super(id, financialInstitutionId);
    }

    public Account(UUID id, String subType, String currency, String description, String reference, String referenceType, Double currentBalance, Double availableBalance, String subType1, String currency1, String description1, String reference1, String referenceType1, Double currentBalance1, Double availableBalance1, FinancialInstitution financialInstitution) {
        super(id, subType, currency, description, reference, referenceType, currentBalance, availableBalance);
    }
}
