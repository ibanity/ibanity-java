package com.ibanity.api;

import com.ibanity.models.CustomerAccessToken;

public interface CustomerAccessTokensService {
    CustomerAccessToken createCustomerAccessToken(CustomerAccessToken customerAccessToken);
}
