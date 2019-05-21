package com.ibanity.apis.client.services;

import com.ibanity.apis.client.models.Account;
import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.models.factory.read.AccountReadQuery;
import com.ibanity.apis.client.models.factory.read.AccountsReadQuery;

public interface AccountsService {

    Account find(AccountReadQuery accountReadQuery);

    IbanityCollection<Account> list(AccountsReadQuery accountsReadQuery);
}
