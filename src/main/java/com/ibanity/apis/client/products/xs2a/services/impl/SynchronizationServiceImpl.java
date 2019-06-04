package com.ibanity.apis.client.products.xs2a.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.mappers.IbanityModelMapper;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.xs2a.models.Synchronization;
import com.ibanity.apis.client.products.xs2a.models.read.SynchronizationReadQuery;
import com.ibanity.apis.client.products.xs2a.services.SynchronizationService;
import com.ibanity.apis.client.services.ApiUrlProvider;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.mapResource;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;
import static org.apache.commons.lang3.StringUtils.removeEnd;

public class SynchronizationServiceImpl implements SynchronizationService {

    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public SynchronizationServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        super();
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public Synchronization create(SynchronizationReadQuery synchronizationReadQuery) {
        Synchronization synchronization = Synchronization.builder()
                .resourceId(synchronizationReadQuery.getResourceId())
                .resourceType(synchronizationReadQuery.getResourceType())
                .subType(synchronizationReadQuery.getSubtype())
                .build();
        String url = getUrl();
        RequestApiModel request = IbanityModelMapper.buildRequest(Synchronization.RESOURCE_TYPE, synchronization);
        String response = ibanityHttpClient.post(buildUri(url), request, synchronizationReadQuery.getAdditionalHeaders(), synchronizationReadQuery.getCustomerAccessToken());
        return mapResource(response, Synchronization.class);
    }

    @Override
    public Synchronization find(SynchronizationReadQuery synchronizationReadQuery) {
            String url = getUrl() + "/" + synchronizationReadQuery.getSynchronizationId();
            String response = ibanityHttpClient.get(buildUri(url), synchronizationReadQuery.getAdditionalHeaders(), synchronizationReadQuery.getCustomerAccessToken());
            return mapResource(response, Synchronization.class);
    }

    private String getUrl() {
        String url = apiUrlProvider.find(IbanityProduct.Xs2a, "customer", "synchronizations");
        return removeEnd(url.replace(Synchronization.API_URL_TAG_ID, ""), "/");
    }
}
