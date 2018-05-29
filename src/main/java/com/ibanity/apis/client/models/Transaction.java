package com.ibanity.apis.client.models;

import com.ibanity.apis.client.paging.PagingBehavior;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import io.crnk.core.resource.annotations.LookupIncludeBehavior;
import io.crnk.core.resource.annotations.SerializeType;

import java.time.Instant;
import java.util.UUID;

@JsonApiResource(type = "transaction", resourcePath = "transactions", pagingBehavior = PagingBehavior.class)
public class Transaction extends AbstractTransaction {

    @JsonApiRelation(lookUp= LookupIncludeBehavior.AUTOMATICALLY_WHEN_NULL,serialize= SerializeType.ONLY_ID)
    private Account account;

    public Transaction() {
        super();
    }

    public Transaction(UUID id,  UUID financialInstitutionAccountId, UUID financialInstitutionId) {
        super(id, financialInstitutionAccountId,financialInstitutionId);
    }

    public Transaction(UUID id, Double amount, String currency, Instant valueDate, Instant executionDate, String description, String remittanceInformationType, String remittanceInformation, String counterpartName, String counterpartReference) {
        super(id, amount, currency, valueDate, executionDate, description, remittanceInformationType, remittanceInformation, counterpartName, counterpartReference);
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
