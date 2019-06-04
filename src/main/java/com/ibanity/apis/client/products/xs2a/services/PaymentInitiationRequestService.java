package com.ibanity.apis.client.products.xs2a.services;

import com.ibanity.apis.client.products.xs2a.models.PaymentInitiationRequest;
import com.ibanity.apis.client.products.xs2a.models.create.PaymentInitiationRequestCreationQuery;
import com.ibanity.apis.client.products.xs2a.models.read.PaymentInitiationRequestReadQuery;

public interface PaymentInitiationRequestService {

    PaymentInitiationRequest create(PaymentInitiationRequestCreationQuery paymentInitiationRequestCreationQuery);

    PaymentInitiationRequest find(PaymentInitiationRequestReadQuery paymentInitiationRequestReadQuery);

    PaymentInitiationRequest delete(PaymentInitiationRequestReadQuery paymentInitiationRequestReadQuery);
}
