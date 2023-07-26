package com.ibanity.apis.client.products.xs2a.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.xs2a.mappers.TransactionDeleteRequestMapper;
import com.ibanity.apis.client.products.xs2a.models.Account;
import com.ibanity.apis.client.products.xs2a.models.create.TransactionDeleteRequestCreationQuery;
import com.ibanity.apis.client.products.xs2a.models.FinancialInstitution;
import com.ibanity.apis.client.products.xs2a.models.TransactionDeleteRequest;
import com.ibanity.apis.client.products.xs2a.services.TransactionDeleteRequestsService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.apache.http.HttpResponse;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.mapResource;
import static com.ibanity.apis.client.mappers.ModelMapperHelper.buildRequest;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;

public class TransactionDeleteRequestsServiceImpl implements TransactionDeleteRequestsService {

    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public TransactionDeleteRequestsServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        super();
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public TransactionDeleteRequest createForApplication(TransactionDeleteRequestCreationQuery transactionDeleteRequestCreationQuery) {
        TransactionDeleteRequest transactionDeleteRequest = TransactionDeleteRequest.builder()
                .beforeDate(transactionDeleteRequestCreationQuery.getBeforeDate())
                .build();
        String url = apiUrlProvider.find(IbanityProduct.Xs2a, "transactionDeleteRequests");
        RequestApiModel request = buildRequest(TransactionDeleteRequest.RESOURCE_TYPE, transactionDeleteRequest);
        HttpResponse response = ibanityHttpClient.post(buildUri(url), request, transactionDeleteRequestCreationQuery.getAdditionalHeaders(), null);
        return mapResource(response, (TransactionDeleteRequestMapper::map));
    }

    @Override
    public TransactionDeleteRequest createForCustomer(TransactionDeleteRequestCreationQuery transactionDeleteRequestCreationQuery) {
        TransactionDeleteRequest transactionDeleteRequest = TransactionDeleteRequest.builder()
                .beforeDate(transactionDeleteRequestCreationQuery.getBeforeDate())
                .build();
        String url = apiUrlProvider.find(IbanityProduct.Xs2a, "customer", "transactionDeleteRequests");
        RequestApiModel request = buildRequest(TransactionDeleteRequest.RESOURCE_TYPE, transactionDeleteRequest);
        HttpResponse response = ibanityHttpClient.post(buildUri(url), request, transactionDeleteRequestCreationQuery.getAdditionalHeaders(), transactionDeleteRequestCreationQuery.getCustomerAccessToken());
        return mapResource(response, (TransactionDeleteRequestMapper::map));
    }

    @Override
    public TransactionDeleteRequest createForAccount(TransactionDeleteRequestCreationQuery transactionDeleteRequestCreationQuery) {
        TransactionDeleteRequest transactionDeleteRequest = TransactionDeleteRequest.builder()
                .beforeDate(transactionDeleteRequestCreationQuery.getBeforeDate())
                .build();
        String url = apiUrlProvider.find(IbanityProduct.Xs2a, "customer", "financialInstitution", "account", "transactionDeleteRequests")
                    .replace(FinancialInstitution.API_URL_TAG_ID, transactionDeleteRequestCreationQuery.getFinancialInstitutionId().toString())
                    .replace(Account.API_URL_TAG_ID, transactionDeleteRequestCreationQuery.getAccountId().toString());
        RequestApiModel request = buildRequest(TransactionDeleteRequest.RESOURCE_TYPE, transactionDeleteRequest);
        HttpResponse response = ibanityHttpClient.post(buildUri(url), request, transactionDeleteRequestCreationQuery.getAdditionalHeaders(), transactionDeleteRequestCreationQuery.getCustomerAccessToken());
        return mapResource(response, (TransactionDeleteRequestMapper::map));
    }
}
