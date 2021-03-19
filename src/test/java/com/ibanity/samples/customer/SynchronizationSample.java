package com.ibanity.samples.customer;

import com.ibanity.apis.client.products.xs2a.models.CustomerAccessToken;
import com.ibanity.apis.client.products.xs2a.models.Synchronization;
import com.ibanity.apis.client.products.xs2a.models.read.SynchronizationCreationQuery;
import com.ibanity.apis.client.products.xs2a.models.read.SynchronizationReadQuery;
import com.ibanity.apis.client.products.xs2a.services.SynchronizationService;
import com.ibanity.apis.client.services.IbanityService;

import java.util.UUID;

public class SynchronizationSample {

    private SynchronizationService synchronizationService;

    public SynchronizationSample(IbanityService ibanityService) {
        this.synchronizationService = ibanityService.xs2aService().synchronizationService();
    }

    public Synchronization create(CustomerAccessToken customerAccessToken, UUID accountId) {
        SynchronizationCreationQuery synchronizationCreationQuery =
                SynchronizationCreationBuilder.builder()
                        .resourceId(accountId.toString())
                        .subtype("accountDetails")
                        .resourceType("account")
                        .customerOnline(true)
                        .customerIpAddress("123.123.123.123")
                        .customerAccessToken(customerAccessToken.getToken())
                        .build();
        return synchronizationService.create(synchronizationCreationQuery);
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
