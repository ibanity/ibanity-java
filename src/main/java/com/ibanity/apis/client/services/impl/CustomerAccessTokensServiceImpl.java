package com.ibanity.apis.client.services.impl;

import com.ibanity.apis.client.models.CustomerAccessToken;
import com.ibanity.apis.client.services.CustomerAccessTokensService;
import io.crnk.core.repository.ResourceRepositoryV2;

import java.util.UUID;

public class CustomerAccessTokensServiceImpl extends AbstractServiceImpl implements CustomerAccessTokensService{

    private final ResourceRepositoryV2<CustomerAccessToken, UUID> customerAccessTokensRepository;

    public CustomerAccessTokensServiceImpl() {
        super();
        customerAccessTokensRepository = getApiClient("/").getRepositoryForType(CustomerAccessToken.class);
    }

    @Override
    public CustomerAccessToken createCustomerAccessToken(CustomerAccessToken customerAccessToken) {
        return customerAccessTokensRepository.create(customerAccessToken);
    }
}
