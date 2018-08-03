package com.ibanity.apis.client.services;

import com.ibanity.apis.client.models.CustomerAccessToken;
import com.ibanity.apis.client.models.factory.create.CustomerAccessTokenCreationQuery;

public interface CustomerAccessTokensService {

    CustomerAccessToken create(CustomerAccessTokenCreationQuery customerAccessTokenCreationQuery);
}
