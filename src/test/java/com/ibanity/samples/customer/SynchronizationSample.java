package com.ibanity.samples.customer;

import com.ibanity.apis.client.helpers.IbanityService;
import com.ibanity.apis.client.models.CustomerAccessToken;
import com.ibanity.apis.client.models.Synchronization;
import com.ibanity.apis.client.models.factory.read.SynchronizationReadQuery;
import com.ibanity.apis.client.services.SynchronizationService;
import com.ibanity.apis.client.services.impl.SynchronizationServiceImpl;

import java.util.UUID;

public class SynchronizationSample {

    private SynchronizationService synchronizationService = new SynchronizationServiceImpl(IbanityService.apiUrlProvider(), IbanityService.ibanityHttpClient());

    public Synchronization create(CustomerAccessToken customerAccessToken, UUID accountId) {
        SynchronizationReadQuery synchronizationReadQuery =
                SynchronizationReadQuery.builder()
                        .resourceId(accountId.toString())
                        .subtype("accountDetails")
                        .resourceType("account")
                        .customerAccessToken(customerAccessToken.getToken())
                        .build();
        return synchronizationService.create(synchronizationReadQuery);
    }

    public Synchronization find(CustomerAccessToken customerAccessToken, UUID synchronizationId) {
        SynchronizationReadQuery synchronizationReadQuery =
                SynchronizationReadQuery.builder()
                        .synchronizationId(synchronizationId)
                        .customerAccessToken(customerAccessToken.getToken())
                        .build();
        return synchronizationService.find(synchronizationReadQuery);
    }
}
