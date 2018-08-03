package com.ibanity.apis.client.services;

import com.ibanity.apis.client.models.Account;
import com.ibanity.apis.client.models.factory.read.AccountReadQuery;
import com.ibanity.apis.client.models.factory.read.AccountsReadQuery;

import java.util.List;

public interface AccountsService {

    Account find(AccountReadQuery accountReadQuery);

    List<Account> list(AccountsReadQuery accountsReadQuery);
}
