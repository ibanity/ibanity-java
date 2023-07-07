package com.ibanity.apis.client.products.xs2a.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.xs2a.mappers.BatchTransactionDeleteRequestMapper;
import com.ibanity.apis.client.products.xs2a.models.BatchTransactionDeleteRequest;
import com.ibanity.apis.client.products.xs2a.models.create.BatchTransactionDeleteRequestCreationQuery;
import com.ibanity.apis.client.products.xs2a.services.BatchTransactionDeleteRequestsService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.apache.http.HttpResponse;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.mapResource;
import static com.ibanity.apis.client.mappers.ModelMapperHelper.buildRequest;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;
import static org.apache.commons.lang3.StringUtils.removeEnd;

public class BatchTransactionDeleteRequestsServiceImpl implements BatchTransactionDeleteRequestsService {

    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public BatchTransactionDeleteRequestsServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        super();
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public BatchTransactionDeleteRequest create(BatchTransactionDeleteRequestCreationQuery batchTransactionDeleteRequestCreationQuery) {
        BatchTransactionDeleteRequest batchTransactionDeleteRequest = BatchTransactionDeleteRequest.builder()
                .beforeDate(batchTransactionDeleteRequestCreationQuery.getBeforeDate())
                .build();
        String url = getUrl();
        RequestApiModel request = buildRequest(BatchTransactionDeleteRequest.RESOURCE_TYPE, batchTransactionDeleteRequest);
        HttpResponse response = ibanityHttpClient.post(buildUri(url), request, batchTransactionDeleteRequestCreationQuery.getAdditionalHeaders(), null);
        return mapResource(response, (BatchTransactionDeleteRequestMapper::map));
    }

    private String getUrl() {
        String url = apiUrlProvider.find(IbanityProduct.Xs2a, "batchTransactionDeleteRequests");
        return removeEnd(url.replace(BatchTransactionDeleteRequest.API_URL_TAG_ID, ""), "/");
    }
}
