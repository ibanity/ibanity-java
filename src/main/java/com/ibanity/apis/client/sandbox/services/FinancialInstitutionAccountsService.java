package com.ibanity.apis.client.sandbox.services;

import com.ibanity.apis.client.sandbox.models.FinancialInstitutionAccount;
import com.ibanity.apis.client.sandbox.models.factory.create.FinancialInstitutionAccountCreationQuery;
import com.ibanity.apis.client.sandbox.models.factory.delete.FinancialInstitutionAccountDeleteQuery;
import com.ibanity.apis.client.sandbox.models.factory.read.FinancialInstitutionAccountReadQuery;
import com.ibanity.apis.client.sandbox.models.factory.read.FinancialInstitutionAccountsReadQuery;

import java.util.List;

public interface FinancialInstitutionAccountsService {

    FinancialInstitutionAccount create(FinancialInstitutionAccountCreationQuery accountCreationQuery);

    List<FinancialInstitutionAccount> list(FinancialInstitutionAccountsReadQuery accountsReadQuery);

    FinancialInstitutionAccount find(FinancialInstitutionAccountReadQuery accountReadQuery);

    void delete(FinancialInstitutionAccountDeleteQuery accountDeleteQuery);

}
