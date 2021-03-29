package com.ibanity.apis.client.products.isabel_connect.services;

import com.ibanity.apis.client.products.isabel_connect.models.BulkPaymentInitiationRequest;
import com.ibanity.apis.client.products.isabel_connect.models.create.BulkPaymentInitiationRequestCreateQuery;

public interface BulkPaymentInitiationRequestService {
    BulkPaymentInitiationRequest create(BulkPaymentInitiationRequestCreateQuery query);
}
