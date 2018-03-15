package com.ibanity.api.impl;

import com.ibanity.api.CustomerAccessTokensService;
import com.ibanity.models.CustomerAccessToken;
import io.crnk.core.repository.ResourceRepositoryV2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

public class CustomerAccessTokensServiceImpl extends AbstractServiceImpl implements CustomerAccessTokensService{
    private static final Logger LOGGER = LogManager.getLogger(CustomerAccessTokensServiceImpl.class);

    private final ResourceRepositoryV2<CustomerAccessToken, UUID> taskRepo;

    public CustomerAccessTokensServiceImpl() {
        super();
        taskRepo = getApiClient("/").getRepositoryForType(CustomerAccessToken.class);
    }

    @Override
    public CustomerAccessToken createCustomerAccessToken(CustomerAccessToken customerAccessToken) {
        return taskRepo.create(customerAccessToken);
    }
}
