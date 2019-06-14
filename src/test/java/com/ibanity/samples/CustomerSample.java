package com.ibanity.samples;

import com.ibanity.apis.client.products.xs2a.models.Customer;
import com.ibanity.apis.client.products.xs2a.models.CustomerAccessToken;
import com.ibanity.apis.client.products.xs2a.models.delete.CustomerDeleteQuery;
import com.ibanity.apis.client.products.xs2a.services.CustomerService;
import com.ibanity.apis.client.services.IbanityService;

public class CustomerSample {

    private final CustomerService customerService;

    public CustomerSample(IbanityService ibanityService) {
        customerService = ibanityService.xs2aService().customerService();
    }

    public Customer delete(CustomerAccessToken customerAccessToken) {
        return customerService.delete(CustomerDeleteQuery.builder()
                .customerAccessToken(customerAccessToken.getToken())
                .build());
    }
}
