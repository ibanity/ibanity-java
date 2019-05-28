package com.ibanity.apis.client.products.xs2a.services;

import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.products.xs2a.models.Account;
import com.ibanity.apis.client.products.xs2a.models.factory.read.AccountReadQuery;
import com.ibanity.apis.client.products.xs2a.models.factory.read.AccountsReadQuery;

public interface AccountsService {

    Account find(AccountReadQuery accountReadQuery);

    IbanityCollection<Account> list(AccountsReadQuery accountsReadQuery);
}
