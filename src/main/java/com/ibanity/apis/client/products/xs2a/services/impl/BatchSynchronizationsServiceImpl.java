package com.ibanity.apis.client.products.xs2a.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.xs2a.mappers.BatchSynchronizationMapper;
import com.ibanity.apis.client.products.xs2a.models.BatchSynchronization;
import com.ibanity.apis.client.products.xs2a.models.create.BatchSynchronizationCreationQuery;
import com.ibanity.apis.client.products.xs2a.models.read.BatchSynchronizationReadQuery;
import com.ibanity.apis.client.products.xs2a.services.BatchSynchronizationsService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.apache.http.HttpResponse;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.mapResource;
import static com.ibanity.apis.client.mappers.ModelMapperHelper.buildRequest;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;
import static org.apache.commons.lang3.StringUtils.removeEnd;

public class BatchSynchronizationsServiceImpl implements BatchSynchronizationsService {

    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public BatchSynchronizationsServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        super();
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public BatchSynchronization create(BatchSynchronizationCreationQuery batchSynchronizationCreationQuery) {
        BatchSynchronization batchSynchronization = BatchSynchronization.builder()
                .resourceType(batchSynchronizationCreationQuery.getResourceType())
                .subtypes(batchSynchronizationCreationQuery.getSubtypes())
                .cancelAfter(batchSynchronizationCreationQuery.getCancelAfter())
                .unlessSynchronizedAfter(batchSynchronizationCreationQuery.getUnlessSynchronizedAfter())
                .build();
        String url = getUrl();
        RequestApiModel request = buildRequest(BatchSynchronization.RESOURCE_TYPE, batchSynchronization);
        HttpResponse response = ibanityHttpClient.post(buildUri(url), request, batchSynchronizationCreationQuery.getAdditionalHeaders(), null);
        return mapResource(response, (BatchSynchronizationMapper::map));
    }

    @Override
    public BatchSynchronization find(BatchSynchronizationReadQuery batchSynchronizationReadQuery) {
        String url = getUrl() + "/" + batchSynchronizationReadQuery.getBatchSynchronizationId();
        HttpResponse response = ibanityHttpClient.get(buildUri(url), batchSynchronizationReadQuery.getAdditionalHeaders(), null);
        return mapResource(response, (BatchSynchronizationMapper::map));
    }

    private String getUrl() {
        String url = apiUrlProvider.find(IbanityProduct.Xs2a, "customer", "batchSynchronizations");
        return removeEnd(url.replace(BatchSynchronization.API_URL_TAG_ID, ""), "/");
    }
}
