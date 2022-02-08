package com.ibanity.apis.client.products.xs2a.services;

import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.products.xs2a.models.Synchronization;
import com.ibanity.apis.client.products.xs2a.models.create.SynchronizationCreationQuery;
import com.ibanity.apis.client.products.xs2a.models.read.SynchronizationReadQuery;
import com.ibanity.apis.client.products.xs2a.models.read.SynchronizationsReadQuery;

public interface SynchronizationService {

    IbanityCollection<Synchronization> list(SynchronizationsReadQuery synchronizationsReadQuery);

    Synchronization create(SynchronizationCreationQuery synchronizationCreationQuery);

    Synchronization find(SynchronizationReadQuery synchronizationReadQuery);
}
