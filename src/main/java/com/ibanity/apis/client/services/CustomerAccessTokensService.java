package com.ibanity.apis.client.services;

import com.ibanity.apis.client.models.CustomerAccessToken;

import java.util.UUID;

public interface CustomerAccessTokensService {

    CustomerAccessToken create(String applicationCustomerReference);

    CustomerAccessToken create(String applicationCustomerReference, UUID idempotencyKey);
}
