package com.ibanity.apis.client.models;

import com.ibanity.apis.client.paging.IbanityPagingBehavior;
import io.crnk.core.resource.annotations.JsonApiResource;

import java.util.UUID;

@JsonApiResource(type = "account", resourcePath = "accounts", pagingBehavior = IbanityPagingBehavior.class)
public class Account extends AbstractAccount {

    public Account() {
        super();
    }

    public Account(final UUID id, final UUID financialInstitutionId) {
        super(id, financialInstitutionId);
    }
}
