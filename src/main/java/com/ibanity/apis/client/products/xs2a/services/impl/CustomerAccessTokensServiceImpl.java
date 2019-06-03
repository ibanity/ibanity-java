package com.ibanity.apis.client.products.xs2a.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.mappers.IbanityModelMapper;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.xs2a.models.CustomerAccessToken;
import com.ibanity.apis.client.products.xs2a.models.factory.create.CustomerAccessTokenCreationQuery;
import com.ibanity.apis.client.products.xs2a.services.CustomerAccessTokensService;
import com.ibanity.apis.client.services.ApiUrlProvider;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.buildRequest;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;

public class CustomerAccessTokensServiceImpl implements CustomerAccessTokensService {

    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public CustomerAccessTokensServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public CustomerAccessToken create(final CustomerAccessTokenCreationQuery customerAccessTokenCreationQuery) {
        CustomerAccessToken customerAccessToken = CustomerAccessToken.builder()
                .applicationCustomerReference(customerAccessTokenCreationQuery.getApplicationCustomerReference())
                .build();

        String url = apiUrlProvider.find(IbanityProduct.Xs2a, "customerAccessTokens");
        RequestApiModel request = buildRequest(CustomerAccessToken.RESOURCE_TYPE, customerAccessToken);
        String response = ibanityHttpClient.post(buildUri(url), request, null);
        return IbanityModelMapper.mapResource(response, CustomerAccessToken.class);
    }
}
