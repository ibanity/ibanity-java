package com.ibanity.apis.client.services.impl;

import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.models.Synchronization;
import com.ibanity.apis.client.models.factory.read.SynchronizationReadQuery;
import com.ibanity.apis.client.services.SynchronizationService;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class SynchronizationServiceImpl extends AbstractServiceImpl implements SynchronizationService {

    public SynchronizationServiceImpl() {
        super();
    }

    @Override
    public Synchronization create(SynchronizationReadQuery synchronizationReadQuery) {
        Synchronization synchronization = new Synchronization();
        synchronization.setResourceType(synchronizationReadQuery.getResourceType());
        synchronization.setResourceId(synchronizationReadQuery.getResourceId());
        synchronization.setSubtype(synchronizationReadQuery.getSubtype());
        return getRepository(synchronizationReadQuery.getCustomerAccessToken()).create(synchronization);
    }

    @Override
    public Synchronization find(SynchronizationReadQuery synchronizationReadQuery) {
        return getRepository(synchronizationReadQuery.getCustomerAccessToken())
                .findOne(synchronizationReadQuery.getSynchronizationId(), new QuerySpec(Synchronization.class));
    }

    private ResourceRepositoryV2<Synchronization, UUID> getRepository(
            final String customerAccessToken) {
        String synchronizations = IbanityConfiguration.getApiUrls().getCustomer().getSynchronizations();
        String finalPath = StringUtils.removeEnd(
                synchronizations
                .replace(Synchronization.RESOURCE_PATH, "")
                .replace(Synchronization.API_URL_TAG_ID, ""), "//");

        return getApiClient(finalPath, customerAccessToken).getRepositoryForType(Synchronization.class);
    }
}
