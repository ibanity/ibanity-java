package com.ibanity.apis.client.services;

import com.ibanity.apis.client.models.CustomerAccessToken;

public interface CustomerAccessTokensService {

    CustomerAccessToken createCustomerAccessToken(CustomerAccessToken customerAccessToken);
}
