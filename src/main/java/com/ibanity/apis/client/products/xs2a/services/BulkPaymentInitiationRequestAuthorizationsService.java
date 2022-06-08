package com.ibanity.apis.client.products.xs2a.services;

import com.ibanity.apis.client.products.xs2a.models.BulkPaymentInitiationRequestAuthorization;
import com.ibanity.apis.client.products.xs2a.models.create.BulkPaymentInitiationRequestAuthorizationCreationQuery;

public interface BulkPaymentInitiationRequestAuthorizationsService {

    BulkPaymentInitiationRequestAuthorization create(BulkPaymentInitiationRequestAuthorizationCreationQuery authorizationCreationQuery);
}
