package com.ibanity.apis.client.products.xs2a.services;

import com.ibanity.apis.client.products.xs2a.models.PaymentInitiationRequest;
import com.ibanity.apis.client.products.xs2a.models.factory.create.PaymentInitiationRequestCreationQuery;
import com.ibanity.apis.client.products.xs2a.models.factory.read.PaymentInitiationRequestReadQuery;

public interface PaymentInitiationRequestService {

    PaymentInitiationRequest create(PaymentInitiationRequestCreationQuery paymentInitiationRequestCreationQuery);

    PaymentInitiationRequest find(PaymentInitiationRequestReadQuery paymentInitiationRequestReadQuery);

    PaymentInitiationRequest delete(PaymentInitiationRequestReadQuery paymentInitiationRequestReadQuery);
}
