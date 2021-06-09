package com.ibanity.apis.client.products.ponto_connect.services;

import com.ibanity.apis.client.products.ponto_connect.models.BulkPayment;
import com.ibanity.apis.client.products.ponto_connect.models.create.BulkPaymentCreateQuery;
import com.ibanity.apis.client.products.ponto_connect.models.delete.BulkPaymentDeleteQuery;
import com.ibanity.apis.client.products.ponto_connect.models.read.BulkPaymentReadQuery;

public interface BulkPaymentService {

    BulkPayment find(BulkPaymentReadQuery bulkPaymentReadQuery);

    BulkPayment create(BulkPaymentCreateQuery bulkPaymentCreateQuery);

    BulkPayment delete(BulkPaymentDeleteQuery bulkPaymentDeleteQuery);
}
