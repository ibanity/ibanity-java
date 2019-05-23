package com.ibanity.samples.sandbox;

import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionAccount;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionTransaction;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionUser;
import com.ibanity.apis.client.sandbox.models.factory.create.FinancialInstitutionTransactionCreationQuery;
import com.ibanity.apis.client.sandbox.models.factory.delete.FinancialInstitutionTransactionDeleteQuery;
import com.ibanity.apis.client.sandbox.models.factory.read.FinancialInstitutionTransactionReadQuery;
import com.ibanity.apis.client.sandbox.services.FinancialInstitutionTransactionsService;
import com.ibanity.apis.client.sandbox.services.impl.FinancialInstitutionTransactionsServiceImpl;
import com.ibanity.samples.helper.SampleHelper;

import java.util.UUID;

public class FinancialInstitutionTransactionSample {
    private final FinancialInstitutionTransactionsService financialInstitutionTransactionsService = new FinancialInstitutionTransactionsServiceImpl(null, null);

    public FinancialInstitutionTransaction create(FinancialInstitution financialInstitution,
                                                  FinancialInstitutionUser financialInstitutionUser,
                                                  FinancialInstitutionAccount financialInstitutionAccount){

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
                       FinancialInstitutionTransaction financialInstitutionTransaction){
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
