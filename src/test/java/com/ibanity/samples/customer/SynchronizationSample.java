package com.ibanity.samples.customer;

import com.ibanity.apis.client.helpers.IbanityService;
import com.ibanity.apis.client.products.xs2a.models.CustomerAccessToken;
import com.ibanity.apis.client.products.xs2a.models.Synchronization;
import com.ibanity.apis.client.products.xs2a.models.factory.read.SynchronizationReadQuery;
import com.ibanity.apis.client.products.xs2a.services.SynchronizationService;
import com.ibanity.apis.client.products.xs2a.services.impl.SynchronizationServiceImpl;

import java.util.UUID;

public class SynchronizationSample {

    private SynchronizationService synchronizationService;

    public SynchronizationSample(IbanityService ibanityService) {
        this.synchronizationService = new SynchronizationServiceImpl(ibanityService.apiUrlProvider(), ibanityService.ibanityHttpClient());
    }

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
