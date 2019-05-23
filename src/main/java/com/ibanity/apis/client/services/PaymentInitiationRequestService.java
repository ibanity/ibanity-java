package com.ibanity.apis.client.services;

import com.ibanity.apis.client.models.PaymentInitiationRequest;
import com.ibanity.apis.client.models.factory.create.PaymentInitiationRequestCreationQuery;
import com.ibanity.apis.client.models.factory.read.PaymentInitiationRequestReadQuery;

public interface PaymentInitiationRequestService {

    PaymentInitiationRequest create(PaymentInitiationRequestCreationQuery paymentInitiationRequestCreationQuery);

    PaymentInitiationRequest find(PaymentInitiationRequestReadQuery paymentInitiationRequestReadQuery);

    PaymentInitiationRequest delete(PaymentInitiationRequestReadQuery paymentInitiationRequestReadQuery);
}
