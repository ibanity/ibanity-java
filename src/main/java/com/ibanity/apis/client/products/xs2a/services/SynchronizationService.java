package com.ibanity.apis.client.products.xs2a.services;

import com.ibanity.apis.client.products.xs2a.models.Synchronization;
import com.ibanity.apis.client.products.xs2a.models.create.SynchronizationCreationQuery;
import com.ibanity.apis.client.products.xs2a.models.read.SynchronizationReadQuery;

public interface SynchronizationService {

    Synchronization create(SynchronizationCreationQuery synchronizationCreationQuery);

    Synchronization find(SynchronizationReadQuery synchronizationReadQuery);
}
