package com.ibanity.apis.client.products.xs2a.services;

import com.ibanity.apis.client.products.xs2a.models.BulkPaymentInitiationRequest;
import com.ibanity.apis.client.products.xs2a.models.create.BulkPaymentInitiationRequestCreationQuery;
import com.ibanity.apis.client.products.xs2a.models.read.BulkPaymentInitiationRequestReadQuery;

public interface BulkPaymentInitiationRequestService {

    BulkPaymentInitiationRequest create(BulkPaymentInitiationRequestCreationQuery bulkPaymentInitiationRequestCreationQuery);

    BulkPaymentInitiationRequest find(BulkPaymentInitiationRequestReadQuery bulkPaymentInitiationRequestReadQuery);

    BulkPaymentInitiationRequest delete(BulkPaymentInitiationRequestReadQuery bulkPaymentInitiationRequestReadQuery);
}
