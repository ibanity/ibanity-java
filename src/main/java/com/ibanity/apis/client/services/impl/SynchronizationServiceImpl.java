package com.ibanity.apis.client.services.impl;

import com.ibanity.apis.client.models.Synchronization;
import com.ibanity.apis.client.models.factory.read.SynchronizationReadQuery;
import com.ibanity.apis.client.network.http.client.IbanityHttpClient;
import com.ibanity.apis.client.services.ApiUrlProvider;
import com.ibanity.apis.client.services.SynchronizationService;

import java.net.URI;
import java.net.URISyntaxException;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.mapResource;
import static org.apache.commons.lang3.StringUtils.removeEnd;

public class SynchronizationServiceImpl implements SynchronizationService {

    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public SynchronizationServiceImpl(IbanityHttpClient ibanityHttpClient, ApiUrlProvider apiUrlProvider) {
        super();
        this.ibanityHttpClient = ibanityHttpClient;
        this.apiUrlProvider = apiUrlProvider;
    }

    @Override
    public Synchronization create(SynchronizationReadQuery synchronizationReadQuery) {
        try {
            Synchronization synchronization = Synchronization.builder()
                    .resourceId(synchronizationReadQuery.getResourceId())
                    .resourceType(synchronizationReadQuery.getResourceType())
                    .subType(synchronizationReadQuery.getSubtype())
                    .build();
            String url = getUrl();
            String response = ibanityHttpClient.post(new URI(url), synchronizationReadQuery.getCustomerAccessToken(), synchronization);
            return mapResource(response, Synchronization.class);
        } catch (URISyntaxException e) {
            throw new IllegalStateException("URL cannot be build", e);
        }
    }

    @Override
    public Synchronization find(SynchronizationReadQuery synchronizationReadQuery) {
        try {
            String url = getUrl() + "/" + synchronizationReadQuery.getSynchronizationId();
            String response = ibanityHttpClient.get(new URI(url), synchronizationReadQuery.getCustomerAccessToken());
            return mapResource(response, Synchronization.class);
        } catch (URISyntaxException e) {
            throw new IllegalStateException("URL cannot be build", e);
        }
    }

    private String getUrl() {
        String url = apiUrlProvider.find("customer", "synchronizations");
        return removeEnd(url.replace(Synchronization.API_URL_TAG_ID, ""), "/");
    }
}
