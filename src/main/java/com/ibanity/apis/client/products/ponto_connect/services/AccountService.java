package com.ibanity.apis.client.products.ponto_connect.services;

import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.products.ponto_connect.models.Account;
import com.ibanity.apis.client.products.ponto_connect.models.read.AccountReadQuery;
import com.ibanity.apis.client.products.ponto_connect.models.read.AccountsReadQuery;

public interface AccountService {

    Account find(AccountReadQuery accountReadQuery);

    IbanityCollection<Account> list(AccountsReadQuery accountsReadQuery);
}
