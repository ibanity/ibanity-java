package com.ibanity.samples.sandbox;

import com.ibanity.apis.client.products.xs2a.models.FinancialInstitution;
import com.ibanity.apis.client.products.xs2a.sandbox.models.FinancialInstitutionAccount;
import com.ibanity.apis.client.products.xs2a.sandbox.models.FinancialInstitutionHolding;
import com.ibanity.apis.client.products.xs2a.sandbox.models.FinancialInstitutionUser;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.create.FinancialInstitutionHoldingCreationQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.delete.FinancialInstitutionHoldingDeleteQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.read.FinancialInstitutionHoldingReadQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.services.FinancialInstitutionHoldingsService;
import com.ibanity.apis.client.services.IbanityService;
import com.ibanity.samples.helper.SampleHelper;

import java.util.UUID;

public class FinancialInstitutionHoldingSample {
    private final FinancialInstitutionHoldingsService financialInstitutionHoldingsService;

    public FinancialInstitutionHoldingSample(IbanityService ibanityService) {
        financialInstitutionHoldingsService = ibanityService.xs2aService().sandbox().financialInstitutionHoldingsService();
    }

    public FinancialInstitutionHolding create(FinancialInstitution financialInstitution,
                                                  FinancialInstitutionUser financialInstitutionUser,
                                                  FinancialInstitutionAccount financialInstitutionAccount) {

        FinancialInstitutionHoldingCreationQuery transactionCreationQuery =
                SampleHelper.generateRandomHoldingCreationQuery(financialInstitution, financialInstitutionUser, financialInstitutionAccount);

        return financialInstitutionHoldingsService.create(transactionCreationQuery);
    }

    public FinancialInstitutionHolding find(FinancialInstitution financialInstitution,
                                                FinancialInstitutionUser financialInstitutionUser,
                                                FinancialInstitutionAccount financialInstitutionAccount,
                                                UUID financialInstitutionHoldingId) {
        FinancialInstitutionHoldingReadQuery transactionReadQuery =
                FinancialInstitutionHoldingReadQuery.builder()
                        .financialInstitutionId(financialInstitution.getId())
                        .financialInstitutionUserId(financialInstitutionUser.getId())
                        .financialInstitutionAccountId(financialInstitutionAccount.getId())
                        .financialInstitutionHoldingId(financialInstitutionHoldingId)
                        .build();

        return financialInstitutionHoldingsService.find(transactionReadQuery);
    }

    public void delete(FinancialInstitution financialInstitution,
                       FinancialInstitutionUser financialInstitutionUser,
                       FinancialInstitutionAccount financialInstitutionAccount,
                       FinancialInstitutionHolding financialInstitutionHolding) {
        FinancialInstitutionHoldingDeleteQuery transactionDeleteQuery =
                FinancialInstitutionHoldingDeleteQuery.builder()
                        .financialInstitutionId(financialInstitution.getId())
                        .financialInstitutionUserId(financialInstitutionUser.getId())
                        .financialInstitutionAccountId(financialInstitutionAccount.getId())
                        .financialInstitutionHoldingId(financialInstitutionHolding.getId())
                        .build();
        financialInstitutionHoldingsService.delete(transactionDeleteQuery);
    }
}
