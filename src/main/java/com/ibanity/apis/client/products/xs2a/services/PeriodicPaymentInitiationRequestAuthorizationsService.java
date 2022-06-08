package com.ibanity.apis.client.products.xs2a.services;

import com.ibanity.apis.client.products.xs2a.models.PeriodicPaymentInitiationRequestAuthorization;
import com.ibanity.apis.client.products.xs2a.models.create.PeriodicPaymentInitiationRequestAuthorizationCreationQuery;

public interface PeriodicPaymentInitiationRequestAuthorizationsService {

    PeriodicPaymentInitiationRequestAuthorization create(PeriodicPaymentInitiationRequestAuthorizationCreationQuery authorizationCreationQuery);
}
