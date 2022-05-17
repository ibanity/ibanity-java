package com.ibanity.apis.client.products.xs2a.services;

import com.ibanity.apis.client.products.xs2a.models.BatchSynchronization;
import com.ibanity.apis.client.products.xs2a.models.create.BatchSynchronizationCreationQuery;

public interface BatchSynchronizationService {

    BatchSynchronization create(BatchSynchronizationCreationQuery batchSynchronizationCreationQuery);
}
