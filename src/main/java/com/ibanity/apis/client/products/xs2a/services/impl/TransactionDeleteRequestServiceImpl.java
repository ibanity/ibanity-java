package com.ibanity.apis.client.products.xs2a.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.xs2a.mappers.TransactionDeleteRequestMapper;
import com.ibanity.apis.client.products.xs2a.models.TransactionDeleteRequest;
import com.ibanity.apis.client.products.xs2a.models.create.TransactionDeleteRequestCreationQuery;
import com.ibanity.apis.client.products.xs2a.services.TransactionDeleteRequestService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.apache.http.HttpResponse;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.mapResource;
import static com.ibanity.apis.client.mappers.ModelMapperHelper.buildRequest;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;
import static org.apache.commons.lang3.StringUtils.removeEnd;

public class TransactionDeleteRequestServiceImpl implements TransactionDeleteRequestService {

    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public TransactionDeleteRequestServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        super();
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public TransactionDeleteRequest create(TransactionDeleteRequestCreationQuery transactionDeleteRequestCreationQuery) {
        TransactionDeleteRequest transactionDeleteRequest = TransactionDeleteRequest.builder()
                .beforeDate(transactionDeleteRequestCreationQuery.getBeforeDate())
                .build();
        String url = getUrl();
        RequestApiModel request = buildRequest(TransactionDeleteRequest.RESOURCE_TYPE, transactionDeleteRequest);
        HttpResponse response = ibanityHttpClient.post(buildUri(url), request, transactionDeleteRequestCreationQuery.getAdditionalHeaders(), transactionDeleteRequestCreationQuery.getCustomerAccessToken());
        return mapResource(response, (TransactionDeleteRequestMapper::map));
    }

    private String getUrl() {
        String url = apiUrlProvider.find(IbanityProduct.Xs2a, "transactionDeleteRequests");
        return removeEnd(url.replace(TransactionDeleteRequest.API_URL_TAG_ID, ""), "/");
    }
}
