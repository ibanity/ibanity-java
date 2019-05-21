package com.ibanity.apis.client.services.impl;

import com.ibanity.apis.client.mappers.IbanityModelMapper;
import com.ibanity.apis.client.models.CustomerAccessToken;
import com.ibanity.apis.client.models.factory.create.CustomerAccessTokenCreationQuery;
import com.ibanity.apis.client.network.http.client.IbanityHttpClient;
import com.ibanity.apis.client.services.ApiUrlProvider;
import com.ibanity.apis.client.services.CustomerAccessTokensService;

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

        String url = apiUrlProvider.find("customerAccessTokens");
        String response = ibanityHttpClient.post(buildUri(url), null, customerAccessToken);

        return IbanityModelMapper.mapResource(response, CustomerAccessToken.class);
    }
}
