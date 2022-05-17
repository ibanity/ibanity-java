package com.ibanity.apis.client.products.xs2a.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.xs2a.mappers.BatchSynchronizationMapper;
import com.ibanity.apis.client.products.xs2a.models.BatchSynchronization;
import com.ibanity.apis.client.products.xs2a.models.Synchronization;
import com.ibanity.apis.client.products.xs2a.models.create.BatchSynchronizationCreationQuery;
import com.ibanity.apis.client.products.xs2a.services.BatchSynchronizationService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.apache.http.HttpResponse;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.mapResource;
import static com.ibanity.apis.client.mappers.ModelMapperHelper.buildRequest;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;
import static org.apache.commons.lang3.StringUtils.removeEnd;

public class BatchSynchronizationServiceImpl implements BatchSynchronizationService {

    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public BatchSynchronizationServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        super();
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public BatchSynchronization create(BatchSynchronizationCreationQuery batchSynchronizationCreationQuery) {
        BatchSynchronization synchronization = BatchSynchronization.builder()
                .resourceType(batchSynchronizationCreationQuery.getResourceType())
                .cancelAfter(batchSynchronizationCreationQuery.getCancelAfter())
                .unlessSynchronizedAfter(batchSynchronizationCreationQuery.getUnlessSynchronizedAfter())
                .resourceType(batchSynchronizationCreationQuery.getResourceType())
                .subtypes(batchSynchronizationCreationQuery.getSubtypes())
                .build();
        String url = getUrl();
        RequestApiModel request = buildRequest(Synchronization.RESOURCE_TYPE, synchronization);
        HttpResponse response = ibanityHttpClient.post(buildUri(url), request, batchSynchronizationCreationQuery.getAdditionalHeaders(), null);
        return mapResource(response, (BatchSynchronizationMapper::map));
    }

    private String getUrl() {
        String url = apiUrlProvider.find(IbanityProduct.Xs2a, "batchSynchronizations");
        return removeEnd(url.replace(BatchSynchronization.API_URL_TAG_ID, ""), "/");
    }
}
