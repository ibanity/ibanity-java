package com.ibanity.apis.client.products.ponto_connect.services;

import com.ibanity.apis.client.products.ponto_connect.models.PaymentRequest;
import com.ibanity.apis.client.products.ponto_connect.models.create.PaymentRequestCreateQuery;
import com.ibanity.apis.client.products.ponto_connect.models.delete.PaymentRequestDeleteQuery;
import com.ibanity.apis.client.products.ponto_connect.models.read.PaymentRequestReadQuery;

public interface PaymentRequestService {
    PaymentRequest find(PaymentRequestReadQuery paymentRequestReadQuery);

    PaymentRequest create(PaymentRequestCreateQuery paymentRequestCreateQuery);

    PaymentRequest delete(PaymentRequestDeleteQuery paymentRequestDeleteQuery);
}
