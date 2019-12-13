package com.ibanity.apis.client.products.ponto_connect.services;

import com.ibanity.apis.client.products.ponto_connect.models.Payment;
import com.ibanity.apis.client.products.ponto_connect.models.create.PaymentCreateQuery;
import com.ibanity.apis.client.products.ponto_connect.models.delete.PaymentDeleteQuery;
import com.ibanity.apis.client.products.ponto_connect.models.read.PaymentReadQuery;

public interface PaymentService {

    Payment find(PaymentReadQuery paymentReadQuery);

    Payment create(PaymentCreateQuery paymentCreateQuery);

    Payment delete(PaymentDeleteQuery paymentDeleteQuery);
}
