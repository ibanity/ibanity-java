package com.ibanity.apis.client.products.ponto_connect.services;

import com.ibanity.apis.client.products.ponto_connect.models.Synchronization;
import com.ibanity.apis.client.products.ponto_connect.models.create.SynchronizationCreateQuery;
import com.ibanity.apis.client.products.ponto_connect.models.read.SynchronizationReadQuery;

public interface SynchronizationService {

    Synchronization create(SynchronizationCreateQuery synchronizationCreateQuery);

    Synchronization find(SynchronizationReadQuery synchronizationReadQuery);
}
