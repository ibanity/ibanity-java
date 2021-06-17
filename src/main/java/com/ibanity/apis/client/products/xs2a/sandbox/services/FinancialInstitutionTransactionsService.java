package com.ibanity.apis.client.products.xs2a.sandbox.services;

import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.products.xs2a.sandbox.models.FinancialInstitutionTransaction;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.create.FinancialInstitutionTransactionCreationQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.delete.FinancialInstitutionTransactionDeleteQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.read.FinancialInstitutionTransactionReadQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.read.FinancialInstitutionTransactionsReadQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.update.FinancialInstitutionTransactionUpdateQuery;

public interface FinancialInstitutionTransactionsService {

    FinancialInstitutionTransaction find(FinancialInstitutionTransactionReadQuery transactionReadQuery);

    IbanityCollection<FinancialInstitutionTransaction> list(FinancialInstitutionTransactionsReadQuery transactionsReadQuery);

    FinancialInstitutionTransaction create(FinancialInstitutionTransactionCreationQuery transactionCreationQuery);

    FinancialInstitutionTransaction update(FinancialInstitutionTransactionUpdateQuery transactionUpdateQuery);

    FinancialInstitutionTransaction delete(FinancialInstitutionTransactionDeleteQuery transactionDeleteQuery);
}
