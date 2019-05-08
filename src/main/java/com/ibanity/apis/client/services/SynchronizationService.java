package com.ibanity.apis.client.services;

import com.ibanity.apis.client.models.Synchronization;
import com.ibanity.apis.client.models.factory.read.SynchronizationReadQuery;

public interface SynchronizationService {

    Synchronization create(SynchronizationReadQuery synchronizationReadQuery);

    Synchronization find(SynchronizationReadQuery synchronizationReadQuery);
}
