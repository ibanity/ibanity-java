package com.ibanity.apis.client.products.ponto_connect.sandbox.services;

import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.products.ponto_connect.sandbox.models.FinancialInstitutionTransaction;
import com.ibanity.apis.client.products.ponto_connect.sandbox.models.factory.create.FinancialInstitutionTransactionCreationQuery;
import com.ibanity.apis.client.products.ponto_connect.sandbox.models.factory.read.FinancialInstitutionTransactionReadQuery;
import com.ibanity.apis.client.products.ponto_connect.sandbox.models.factory.read.FinancialInstitutionTransactionsReadQuery;
import com.ibanity.apis.client.products.ponto_connect.sandbox.models.factory.update.FinancialInstitutionTransactionUpdateQuery;

public interface FinancialInstitutionTransactionsService {

    FinancialInstitutionTransaction find(FinancialInstitutionTransactionReadQuery transactionReadQuery);

    IbanityCollection<FinancialInstitutionTransaction> list(FinancialInstitutionTransactionsReadQuery transactionsReadQuery);

    FinancialInstitutionTransaction create(FinancialInstitutionTransactionCreationQuery transactionCreationQuery);

    FinancialInstitutionTransaction update(FinancialInstitutionTransactionUpdateQuery transactionUpdateQuery);
}
