package com.ibanity.apis.client.services.impl;

import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.models.CustomerAccessToken;
import com.ibanity.apis.client.services.CustomerAccessTokensService;
import io.crnk.core.repository.ResourceRepositoryV2;

import java.util.UUID;

public class CustomerAccessTokensServiceImpl extends AbstractServiceImpl implements CustomerAccessTokensService {

    public CustomerAccessTokensServiceImpl() {
        super();
    }

    @Override
    public CustomerAccessToken create(final String applicationCustomerReference) {
        return create(applicationCustomerReference, null);
    }

    @Override
    public CustomerAccessToken create(final String applicationCustomerReference, final UUID idempotencyKey) {
        CustomerAccessToken customerAccessToken = new CustomerAccessToken();
        customerAccessToken.setApplicationCustomerReference(applicationCustomerReference);
        return getCustomerAccessTokensRepo(idempotencyKey).create(customerAccessToken);
    }

    protected ResourceRepositoryV2<CustomerAccessToken, UUID> getCustomerAccessTokensRepo(final UUID idempotencyKey) {
        String finalPath = IbanityConfiguration.getApiIUrls().getCustomerAccessTokens().replace(CustomerAccessToken.RESOURCE_PATH, "");

        return getApiClient(finalPath, null, idempotencyKey).getRepositoryForType(CustomerAccessToken.class);
    }


}
