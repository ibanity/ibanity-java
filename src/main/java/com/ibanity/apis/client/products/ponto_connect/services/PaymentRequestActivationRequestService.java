package com.ibanity.apis.client.products.ponto_connect.services;

import com.ibanity.apis.client.products.ponto_connect.models.create.PaymentRequestActivationRequestCreateQuery;

public interface PaymentRequestActivationRequestService {

    com.ibanity.apis.client.products.ponto_connect.models.PaymentRequestActivationRequest create(PaymentRequestActivationRequestCreateQuery paymentRequestActivationRequestCreateQuery);
}
