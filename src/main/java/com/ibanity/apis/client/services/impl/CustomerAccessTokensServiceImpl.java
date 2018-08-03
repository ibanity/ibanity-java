package com.ibanity.apis.client.services.impl;

import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.models.CustomerAccessToken;
import com.ibanity.apis.client.models.factory.create.CustomerAccessTokenCreationQuery;
import com.ibanity.apis.client.services.CustomerAccessTokensService;
import io.crnk.core.repository.ResourceRepositoryV2;

import java.util.UUID;

public class CustomerAccessTokensServiceImpl extends AbstractServiceImpl implements CustomerAccessTokensService {

    public CustomerAccessTokensServiceImpl() {
        super();
    }

    @Override
    public CustomerAccessToken create(final CustomerAccessTokenCreationQuery customerAccessTokenCreationQuery) {
        CustomerAccessToken customerAccessToken = new CustomerAccessToken();
        customerAccessToken.setApplicationCustomerReference(customerAccessTokenCreationQuery.getApplicationCustomerReference());

        return getRepository(customerAccessTokenCreationQuery.getIdempotencyKey())
                .create(customerAccessToken);
    }

    private ResourceRepositoryV2<CustomerAccessToken, UUID> getRepository(final UUID idempotencyKey) {
        String finalPath = IbanityConfiguration.getApiUrls().getCustomerAccessTokens()
                .replace(CustomerAccessToken.RESOURCE_PATH, "");

        return getApiClient(finalPath, null, idempotencyKey).getRepositoryForType(CustomerAccessToken.class);
    }


}
