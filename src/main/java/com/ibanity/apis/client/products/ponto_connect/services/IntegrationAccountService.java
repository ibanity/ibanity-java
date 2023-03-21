package com.ibanity.apis.client.products.ponto_connect.services;

import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.products.ponto_connect.models.IntegrationAccount;
import com.ibanity.apis.client.products.ponto_connect.models.read.IntegrationAccountsReadQuery;

public interface IntegrationAccountService {

    IbanityCollection<IntegrationAccount> list(IntegrationAccountsReadQuery integrationAccountsReadQuery);

}
