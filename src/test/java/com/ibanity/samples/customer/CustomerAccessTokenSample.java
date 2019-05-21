package com.ibanity.samples.customer;

import com.ibanity.apis.client.models.CustomerAccessToken;
import com.ibanity.apis.client.models.factory.create.CustomerAccessTokenCreationQuery;
import com.ibanity.apis.client.services.CustomerAccessTokensService;
import com.ibanity.apis.client.services.impl.CustomerAccessTokensServiceImpl;

public class CustomerAccessTokenSample {
    private final CustomerAccessTokensService customerAccessTokensService = new CustomerAccessTokensServiceImpl(null, null);

    public CustomerAccessToken create(String consentReference) {
        CustomerAccessTokenCreationQuery customerAccessTokenCreationQuery =
                CustomerAccessTokenCreationQuery.builder().applicationCustomerReference(consentReference).build();

        return customerAccessTokensService.create(customerAccessTokenCreationQuery);
    }
}
