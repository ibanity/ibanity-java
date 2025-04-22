package com.ibanity.apis.client.products.ponto_connect.services;

import com.ibanity.apis.client.products.ponto_connect.models.PaymentRequestActivationRequest;
import com.ibanity.apis.client.products.ponto_connect.models.create.PaymentRequestActivationRequestCreateQuery;

public interface PaymentRequestActivationRequestService {

    PaymentRequestActivationRequest create(PaymentRequestActivationRequestCreateQuery paymentRequestActivationRequestCreateQuery);

}
