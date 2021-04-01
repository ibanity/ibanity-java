package com.ibanity.apis.client.products.isabel_connect.services;

import com.ibanity.apis.client.products.isabel_connect.models.BulkPaymentInitiationRequest;
import com.ibanity.apis.client.products.isabel_connect.models.create.BulkPaymentInitiationRequestCreateQuery;
import com.ibanity.apis.client.products.isabel_connect.models.read.BulkPaymentInitiationRequestReadQuery;

public interface BulkPaymentInitiationRequestService {
    BulkPaymentInitiationRequest create(BulkPaymentInitiationRequestCreateQuery query);

    BulkPaymentInitiationRequest find(BulkPaymentInitiationRequestReadQuery query);
}
