package com.ibanity.apis.client.products.isabel_connect.services;

import com.ibanity.apis.client.products.isabel_connect.models.read.PaymentStatusReadQuery;

public interface PaymentStatusService {
    String find(PaymentStatusReadQuery query);
}
