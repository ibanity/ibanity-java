package com.ibanity.apis.client.products.xs2a.services;

import com.ibanity.apis.client.products.xs2a.models.BatchSynchronization;
import com.ibanity.apis.client.products.xs2a.models.create.BatchSynchronizationCreationQuery;
import com.ibanity.apis.client.products.xs2a.models.read.BatchSynchronizationReadQuery;

public interface BatchSynchronizationsService {

    BatchSynchronization create(BatchSynchronizationCreationQuery batchSynchronizationCreationQuery);

    BatchSynchronization find(BatchSynchronizationReadQuery batchSynchronizationReadQuery);
}
