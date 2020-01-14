package com.ibanity.apis.client.products.xs2a.services;

import com.ibanity.apis.client.products.xs2a.models.PaymentInitiationRequestAuthorization;
import com.ibanity.apis.client.products.xs2a.models.create.PaymentInitiationRequestAuthorizationCreationQuery;

public interface PaymentInitiationRequestAuthorizationsService {

    PaymentInitiationRequestAuthorization create(PaymentInitiationRequestAuthorizationCreationQuery authorizationCreationQuery);
}
