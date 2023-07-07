package com.ibanity.apis.client.products.xs2a.services;

import com.ibanity.apis.client.products.xs2a.models.BatchTransactionDeleteRequest;
import com.ibanity.apis.client.products.xs2a.models.create.BatchTransactionDeleteRequestCreationQuery;

public interface BatchTransactionDeleteRequestService {

    BatchTransactionDeleteRequest create(BatchTransactionDeleteRequestCreationQuery batchTransactionDeleteRequestCreationQuery);
}
