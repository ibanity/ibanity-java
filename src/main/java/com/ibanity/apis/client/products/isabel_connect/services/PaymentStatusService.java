package com.ibanity.apis.client.products.isabel_connect.services;

import com.ibanity.apis.client.products.isabel_connect.models.PaymentStatusResult;
import com.ibanity.apis.client.products.isabel_connect.models.read.PaymentStatusReadQuery;

public interface PaymentStatusService {
    PaymentStatusResult find(PaymentStatusReadQuery query);
}
