package com.ibanity.apis.client.services.impl;

import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.models.CustomerAccessToken;
import com.ibanity.apis.client.services.CustomerAccessTokensService;

public class CustomerAccessTokensServiceImpl extends AbstractServiceImpl implements CustomerAccessTokensService {

    public CustomerAccessTokensServiceImpl() {
        super();
    }

    @Override
    public CustomerAccessToken createCustomerAccessToken(final CustomerAccessToken customerAccessToken) {
        String finalPath = IbanityConfiguration.getApiIUrls().getCustomerAccessTokens().replace(CustomerAccessToken.RESOURCE_PATH, "");

        return getApiClient(finalPath).getRepositoryForType(CustomerAccessToken.class).create(customerAccessToken);
    }
}
