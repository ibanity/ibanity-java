package com.ibanity.samples.sandbox;

import com.ibanity.apis.client.products.xs2a.models.FinancialInstitution;
import com.ibanity.apis.client.products.xs2a.sandbox.models.FinancialInstitutionAccount;
import com.ibanity.apis.client.products.xs2a.sandbox.models.FinancialInstitutionTransaction;
import com.ibanity.apis.client.products.xs2a.sandbox.models.FinancialInstitutionUser;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.create.FinancialInstitutionTransactionCreationQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.delete.FinancialInstitutionTransactionDeleteQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.read.FinancialInstitutionTransactionReadQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.services.FinancialInstitutionTransactionsService;
import com.ibanity.apis.client.services.IbanityService;
import com.ibanity.samples.helper.SampleHelper;

import java.util.UUID;

public class FinancialInstitutionTransactionSample {
    private final FinancialInstitutionTransactionsService financialInstitutionTransactionsService;

    public FinancialInstitutionTransactionSample(IbanityService ibanityService) {
        financialInstitutionTransactionsService = ibanityService.xs2aService().sandbox().financialInstitutionTransactionsService();
    }

    public FinancialInstitutionTransaction create(FinancialInstitution financialInstitution,
                                                  FinancialInstitutionUser financialInstitutionUser,
                                                  FinancialInstitutionAccount financialInstitutionAccount) {

        FinancialInstitutionTransactionCreationQuery transactionCreationQuery =
                SampleHelper.generateRandomTransactionCreationQuery(financialInstitution, financialInstitutionUser, financialInstitutionAccount);

        return financialInstitutionTransactionsService.create(transactionCreationQuery);
    }

    public FinancialInstitutionTransaction find(FinancialInstitution financialInstitution,
                                                FinancialInstitutionUser financialInstitutionUser,
                                                FinancialInstitutionAccount financialInstitutionAccount,
                                                UUID financialInstitutionTransactionId) {
        FinancialInstitutionTransactionReadQuery transactionReadQuery =
                FinancialInstitutionTransactionReadQuery.builder()
                        .financialInstitutionId(financialInstitution.getId())
                        .financialInstitutionUserId(financialInstitutionUser.getId())
                        .financialInstitutionAccountId(financialInstitutionAccount.getId())
                        .financialInstitutionTransactionId(financialInstitutionTransactionId)
                        .build();

        return financialInstitutionTransactionsService.find(transactionReadQuery);
    }

    public void delete(FinancialInstitution financialInstitution,
                       FinancialInstitutionUser financialInstitutionUser,
                       FinancialInstitutionAccount financialInstitutionAccount,
                       FinancialInstitutionTransaction financialInstitutionTransaction) {
        FinancialInstitutionTransactionDeleteQuery transactionDeleteQuery =
                FinancialInstitutionTransactionDeleteQuery.builder()
                        .financialInstitutionId(financialInstitution.getId())
                        .financialInstitutionUserId(financialInstitutionUser.getId())
                        .financialInstitutionAccountId(financialInstitutionAccount.getId())
                        .financialInstitutionTransactionId(financialInstitutionTransaction.getId())
                        .build();
        financialInstitutionTransactionsService.delete(transactionDeleteQuery);
    }
}
