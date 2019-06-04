package com.ibanity.apis.client.products.xs2a.services;

import com.ibanity.apis.client.products.xs2a.models.CustomerAccessToken;
import com.ibanity.apis.client.products.xs2a.models.create.CustomerAccessTokenCreationQuery;

public interface CustomerAccessTokensService {

    CustomerAccessToken create(CustomerAccessTokenCreationQuery customerAccessTokenCreationQuery);
}
