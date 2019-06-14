package com.ibanity.apis.client.products.xs2a.services;

import com.ibanity.apis.client.products.xs2a.models.Customer;
import com.ibanity.apis.client.products.xs2a.models.delete.CustomerDeleteQuery;

public interface CustomerService {

    Customer delete(CustomerDeleteQuery customerDeleteQuery);
}
