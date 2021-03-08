package com.ibanity.apis.client.products.isabel_connect.services;

import com.ibanity.apis.client.products.isabel_connect.models.Account;
import com.ibanity.apis.client.products.isabel_connect.models.read.AccountReadQuery;

public interface AccountsService {
    Account find(AccountReadQuery query);
}
