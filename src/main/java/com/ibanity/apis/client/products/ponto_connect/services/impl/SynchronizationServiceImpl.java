package com.ibanity.apis.client.products.ponto_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.mappers.IbanityModelMapper;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.ponto_connect.models.Synchronization;
import com.ibanity.apis.client.products.ponto_connect.models.create.SynchronizationCreateQuery;
import com.ibanity.apis.client.products.ponto_connect.models.read.SynchronizationReadQuery;
import com.ibanity.apis.client.products.ponto_connect.services.SynchronizationService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.mapResource;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;

public class SynchronizationServiceImpl implements SynchronizationService {

    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public SynchronizationServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public Synchronization create(SynchronizationCreateQuery synchronizationCreateQuery) {
        Synchronization synchronization = Synchronization.builder()
                .resourceId(synchronizationCreateQuery.getResourceId())
                .resourceType(synchronizationCreateQuery.getResourceType())
                .subtype(synchronizationCreateQuery.getSubtype())
                .build();
        RequestApiModel request = IbanityModelMapper.buildRequest(Synchronization.RESOURCE_TYPE, synchronization);
        HttpResponse response = ibanityHttpClient.post(buildUri(getUrl()), request, synchronizationCreateQuery.getAdditionalHeaders(), synchronizationCreateQuery.getAccessToken());
        return mapResource(response, Synchronization.class);
    }

    @Override
    public Synchronization find(SynchronizationReadQuery synchronizationReadQuery) {
        String url = getUrl()
                + "/"
                + synchronizationReadQuery.getSynchronizationId();
        HttpResponse response = ibanityHttpClient.get(buildUri(url), synchronizationReadQuery.getAdditionalHeaders(), synchronizationReadQuery.getAccessToken());
        return mapResource(response, Synchronization.class);
    }

    private String getUrl() {
        String url = apiUrlProvider
                .find(IbanityProduct.PontoConnect, "synchronizations")
                .replace(Synchronization.API_URL_TAG_ID, "");
        return StringUtils.removeEnd(url, "/");
    }
}
