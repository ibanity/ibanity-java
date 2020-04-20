package com.ibanity.apis.client.products.ponto_connect.sandbox.services;

import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.products.ponto_connect.sandbox.models.FinancialInstitutionAccount;
import com.ibanity.apis.client.products.ponto_connect.sandbox.models.factory.read.FinancialInstitutionAccountReadQuery;
import com.ibanity.apis.client.products.ponto_connect.sandbox.models.factory.read.FinancialInstitutionAccountsReadQuery;

public interface FinancialInstitutionAccountsService {

    IbanityCollection<FinancialInstitutionAccount> list(FinancialInstitutionAccountsReadQuery accountsReadQuery);

    FinancialInstitutionAccount find(FinancialInstitutionAccountReadQuery accountReadQuery);

}
