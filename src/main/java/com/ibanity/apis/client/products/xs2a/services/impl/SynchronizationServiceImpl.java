package com.ibanity.apis.client.products.xs2a.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.mappers.IbanityModelMapper;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.xs2a.mappers.SynchronizationMapper;
import com.ibanity.apis.client.products.xs2a.models.Synchronization;
import com.ibanity.apis.client.products.xs2a.models.create.SynchronizationCreationQuery;
import com.ibanity.apis.client.products.xs2a.models.read.SynchronizationReadQuery;
import com.ibanity.apis.client.products.xs2a.services.SynchronizationService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.apache.http.HttpResponse;

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
    public Synchronization create(SynchronizationCreationQuery synchronizationCreationQuery) {
        Synchronization synchronization = Synchronization.builder()
                .resourceId(synchronizationCreationQuery.getResourceId())
                .resourceType(synchronizationCreationQuery.getResourceType())
                .subtype(synchronizationCreationQuery.getSubtype())
                .customerOnline(synchronizationCreationQuery.isCustomerOnline())
                .customerIpAddress(synchronizationCreationQuery.getCustomerIpAddress())
                .build();
        String url = getUrl();
        RequestApiModel request = IbanityModelMapper.buildRequest(Synchronization.RESOURCE_TYPE, synchronization);
        HttpResponse response = ibanityHttpClient.post(buildUri(url), request, synchronizationCreationQuery.getAdditionalHeaders(), synchronizationCreationQuery.getCustomerAccessToken());
        return mapResource(response, (SynchronizationMapper::map));
    }

    @Override
    public Synchronization find(SynchronizationReadQuery synchronizationReadQuery) {
        String url = getUrl() + "/" + synchronizationReadQuery.getSynchronizationId();
        HttpResponse response = ibanityHttpClient.get(buildUri(url), synchronizationReadQuery.getAdditionalHeaders(), synchronizationReadQuery.getCustomerAccessToken());
        return mapResource(response, (SynchronizationMapper::map));
    }

    private String getUrl() {
        String url = apiUrlProvider.find(IbanityProduct.Xs2a, "customer", "synchronizations");
        return removeEnd(url.replace(Synchronization.API_URL_TAG_ID, ""), "/");
    }
}
