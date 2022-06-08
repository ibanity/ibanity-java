package com.ibanity.apis.client.products.xs2a.services;

import com.ibanity.apis.client.products.xs2a.models.PeriodicPaymentInitiationRequest;
import com.ibanity.apis.client.products.xs2a.models.create.PeriodicPaymentInitiationRequestCreationQuery;
import com.ibanity.apis.client.products.xs2a.models.read.PeriodicPaymentInitiationRequestReadQuery;

public interface PeriodicPaymentInitiationRequestService {

    PeriodicPaymentInitiationRequest create(PeriodicPaymentInitiationRequestCreationQuery periodicPaymentInitiationRequestCreationQuery);

    PeriodicPaymentInitiationRequest find(PeriodicPaymentInitiationRequestReadQuery periodicPaymentInitiationRequestReadQuery);

    PeriodicPaymentInitiationRequest delete(PeriodicPaymentInitiationRequestReadQuery periodicPaymentInitiationRequestReadQuery);
}
