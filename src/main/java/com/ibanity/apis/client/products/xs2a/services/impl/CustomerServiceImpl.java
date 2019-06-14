package com.ibanity.apis.client.products.xs2a.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.mappers.IbanityModelMapper;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.xs2a.models.Customer;
import com.ibanity.apis.client.products.xs2a.models.delete.CustomerDeleteQuery;
import com.ibanity.apis.client.products.xs2a.services.CustomerService;
import com.ibanity.apis.client.services.ApiUrlProvider;

import static com.ibanity.apis.client.utils.URIHelper.buildUri;

public class CustomerServiceImpl implements CustomerService {

    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public CustomerServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public Customer delete(CustomerDeleteQuery customerDeleteQuery) {
        String url =  apiUrlProvider.find(IbanityProduct.Xs2a, "customer", "self");
        String response = ibanityHttpClient.delete(buildUri(url), customerDeleteQuery.getAdditionalHeaders(), customerDeleteQuery.getCustomerAccessToken());
        return IbanityModelMapper.mapResource(response, Customer.class);
    }
}
