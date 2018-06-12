package com.ibanity.apis.client.models;

import com.ibanity.apis.client.paging.IbanityPagingBehavior;
import io.crnk.core.resource.annotations.JsonApiResource;

import java.util.UUID;

@JsonApiResource(type = "account", resourcePath = "accounts", pagingBehavior = IbanityPagingBehavior.class)
public class Account extends AbstractAccount {

    public Account() {
        super();
    }

    public Account(UUID id, UUID financialInstitutionId) {
        super(id, financialInstitutionId);
    }
}
