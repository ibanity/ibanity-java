package com.ibanity.apis.client.services.impl;

import com.ibanity.apis.client.services.CustomerAccessTokensService;
import com.ibanity.apis.client.models.CustomerAccessToken;
import io.crnk.core.repository.ResourceRepositoryV2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

public class CustomerAccessTokensServiceImpl extends AbstractServiceImpl implements CustomerAccessTokensService{
    private static final Logger LOGGER = LogManager.getLogger(CustomerAccessTokensServiceImpl.class);

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
