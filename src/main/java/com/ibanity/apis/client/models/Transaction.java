package com.ibanity.apis.client.models;

import com.ibanity.apis.client.paging.PagingBehavior;
import io.crnk.core.resource.annotations.JsonApiResource;

import java.time.Instant;
import java.util.UUID;

@JsonApiResource(type = "transaction", resourcePath = "transactions", pagingBehavior = PagingBehavior.class)
public class Transaction extends AbstractTransaction {


    public Transaction() {
        super();
    }

    public Transaction(UUID id,  UUID financialInstitutionAccountId, UUID financialInstitutionId) {
        super(id, financialInstitutionAccountId,financialInstitutionId);
    }

    public Transaction(UUID id, Double amount, String currency, Instant valueDate, Instant executionDate, String description, String remittanceInformationType, String remittanceInformation, String counterpartName, String counterpartReference) {
        super(id, amount, currency, valueDate, executionDate, description, remittanceInformationType, remittanceInformation, counterpartName, counterpartReference);
    }
}
