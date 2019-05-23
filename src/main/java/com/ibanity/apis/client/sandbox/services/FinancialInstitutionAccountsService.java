package com.ibanity.apis.client.sandbox.services;

import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionAccount;
import com.ibanity.apis.client.sandbox.models.factory.create.FinancialInstitutionAccountCreationQuery;
import com.ibanity.apis.client.sandbox.models.factory.delete.FinancialInstitutionAccountDeleteQuery;
import com.ibanity.apis.client.sandbox.models.factory.read.FinancialInstitutionAccountReadQuery;
import com.ibanity.apis.client.sandbox.models.factory.read.FinancialInstitutionAccountsReadQuery;

public interface FinancialInstitutionAccountsService {

    FinancialInstitutionAccount create(FinancialInstitutionAccountCreationQuery accountCreationQuery);

    IbanityCollection<FinancialInstitutionAccount> list(FinancialInstitutionAccountsReadQuery accountsReadQuery);

    FinancialInstitutionAccount find(FinancialInstitutionAccountReadQuery accountReadQuery);

    FinancialInstitutionAccount delete(FinancialInstitutionAccountDeleteQuery accountDeleteQuery);

}
