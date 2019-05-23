package com.ibanity.apis.client.sandbox.services;

import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionTransaction;
import com.ibanity.apis.client.sandbox.models.factory.create.FinancialInstitutionTransactionCreationQuery;
import com.ibanity.apis.client.sandbox.models.factory.delete.FinancialInstitutionTransactionDeleteQuery;
import com.ibanity.apis.client.sandbox.models.factory.read.FinancialInstitutionTransactionReadQuery;
import com.ibanity.apis.client.sandbox.models.factory.read.FinancialInstitutionTransactionsReadQuery;

public interface FinancialInstitutionTransactionsService {

    FinancialInstitutionTransaction find(FinancialInstitutionTransactionReadQuery transactionReadQuery);

    IbanityCollection<FinancialInstitutionTransaction> list(FinancialInstitutionTransactionsReadQuery transactionsReadQuery);

    FinancialInstitutionTransaction create(FinancialInstitutionTransactionCreationQuery transactionCreationQuery);

    FinancialInstitutionTransaction delete(FinancialInstitutionTransactionDeleteQuery transactionDeleteQuery);
}
