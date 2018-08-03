package com.ibanity.apis.client.sandbox.services;

import com.ibanity.apis.client.sandbox.models.FinancialInstitutionTransaction;
import com.ibanity.apis.client.sandbox.models.factory.create.FinancialInstitutionTransactionCreationQuery;
import com.ibanity.apis.client.sandbox.models.factory.delete.FinancialInstitutionTransactionDeleteQuery;
import com.ibanity.apis.client.sandbox.models.factory.read.FinancialInstitutionTransactionReadQuery;
import com.ibanity.apis.client.sandbox.models.factory.read.FinancialInstitutionTransactionsReadQuery;

import java.util.List;

public interface FinancialInstitutionTransactionsService {

    FinancialInstitutionTransaction find(FinancialInstitutionTransactionReadQuery transactionReadQuery);

    List<FinancialInstitutionTransaction> list(FinancialInstitutionTransactionsReadQuery transactionsReadQuery);

    FinancialInstitutionTransaction create(FinancialInstitutionTransactionCreationQuery transactionCreationQuery);

    void delete(FinancialInstitutionTransactionDeleteQuery transactionDeleteQuery);
}
