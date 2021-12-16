package com.ibanity.apis.client.products.ponto_connect.services;

import com.ibanity.apis.client.products.ponto_connect.models.create.PaymentActivationRequestCreateQuery;

public interface PaymentActivationRequestService {

    com.ibanity.apis.client.products.ponto_connect.models.PaymentActivationRequest create(PaymentActivationRequestCreateQuery paymentActivationRequestCreateQuery);
}
