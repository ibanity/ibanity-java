package com.ibanity.apis.client.services.impl;

import com.ibanity.apis.client.models.CustomerAccessToken;
import com.ibanity.apis.client.services.CustomerAccessTokensService;

public class CustomerAccessTokensServiceImpl extends AbstractServiceImpl implements CustomerAccessTokensService{

    public CustomerAccessTokensServiceImpl() {
        super();
    }

    @Override
    public CustomerAccessToken createCustomerAccessToken(CustomerAccessToken customerAccessToken) {
        return getApiClient("/").getRepositoryForType(CustomerAccessToken.class).create(customerAccessToken);
    }
}
