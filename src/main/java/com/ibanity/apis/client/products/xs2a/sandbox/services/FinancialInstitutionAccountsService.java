package com.ibanity.apis.client.products.xs2a.sandbox.services;

import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.products.xs2a.sandbox.models.FinancialInstitutionAccount;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.create.FinancialInstitutionAccountCreationQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.delete.FinancialInstitutionAccountDeleteQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.read.FinancialInstitutionAccountReadQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.read.FinancialInstitutionAccountsReadQuery;

public interface FinancialInstitutionAccountsService {

    FinancialInstitutionAccount create(FinancialInstitutionAccountCreationQuery accountCreationQuery);

    IbanityCollection<FinancialInstitutionAccount> list(FinancialInstitutionAccountsReadQuery accountsReadQuery);

    FinancialInstitutionAccount find(FinancialInstitutionAccountReadQuery accountReadQuery);

    FinancialInstitutionAccount delete(FinancialInstitutionAccountDeleteQuery accountDeleteQuery);

}
