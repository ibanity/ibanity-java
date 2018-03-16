package com.ibanity.api;

import com.ibanity.models.CustomerAccessToken;

/**
 * Service to manage the Authorization Token necessary to access iBanity APIs
 */
public interface CustomerAccessTokensService {

    /**
     * Create a customer access token to be used as Authorization header in the HTTPs requests
     * Other methods in this Java Client library will require to have that CustomerAccessToken
     * @param customerAccessToken the initial instance that contains the TPP application_customer_reference
     * @return an AccessToken containing the token to be used for the next flow's services.
     */
    CustomerAccessToken createCustomerAccessToken(CustomerAccessToken customerAccessToken);
}
