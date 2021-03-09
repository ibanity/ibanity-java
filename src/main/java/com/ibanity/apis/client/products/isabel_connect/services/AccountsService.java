package com.ibanity.apis.client.products.isabel_connect.services;

import com.ibanity.apis.client.models.IsabelCollection;
import com.ibanity.apis.client.products.isabel_connect.models.Account;
import com.ibanity.apis.client.products.isabel_connect.models.read.AccountReadQuery;
import com.ibanity.apis.client.products.isabel_connect.models.read.AccountsReadQuery;

public interface AccountsService {
    Account find(AccountReadQuery query);

    IsabelCollection<Account> list(AccountsReadQuery query);
}
