package com.ibanity.apis.client.models;

import com.ibanity.apis.client.paging.IBanityPagingBehavior;
import io.crnk.core.resource.annotations.JsonApiResource;

import java.util.UUID;

@JsonApiResource(type = "account", resourcePath = "accounts", pagingBehavior = IBanityPagingBehavior.class)
public class Account extends AbstractAccount {

    public Account() {
        super();
    }

    public Account(UUID id, UUID financialInstitutionId) {
        super(id, financialInstitutionId);
    }
}
