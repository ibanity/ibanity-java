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
        return getRepository(idempotencyKey).create(customerAccessToken);
    }

    private ResourceRepositoryV2<CustomerAccessToken, UUID> getRepository(final UUID idempotencyKey) {
        String finalPath = IbanityConfiguration.getApiUrls().getCustomerAccessTokens()
                .replace(CustomerAccessToken.RESOURCE_PATH, "");

        return getApiClient(finalPath, null, idempotencyKey).getRepositoryForType(CustomerAccessToken.class);
    }


}
