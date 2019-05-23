package com.ibanity.samples.sandbox;

import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionAccount;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionUser;
import com.ibanity.apis.client.sandbox.models.factory.create.FinancialInstitutionAccountCreationQuery;
import com.ibanity.apis.client.sandbox.models.factory.delete.FinancialInstitutionAccountDeleteQuery;
import com.ibanity.apis.client.sandbox.models.factory.read.FinancialInstitutionAccountReadQuery;
import com.ibanity.apis.client.sandbox.services.FinancialInstitutionAccountsService;
import com.ibanity.apis.client.sandbox.services.impl.FinancialInstitutionAccountsServiceImpl;
import com.ibanity.samples.helper.SampleHelper;
import org.iban4j.CountryCode;
import org.iban4j.Iban;

import java.util.UUID;

public class FinancialInstitutionAccountSample {
    private final FinancialInstitutionAccountsService financialInstitutionAccountsService = new FinancialInstitutionAccountsServiceImpl(null, null);

    public FinancialInstitutionAccount create(FinancialInstitution financialInstitution, FinancialInstitutionUser financialInstitutionUser){
        FinancialInstitutionAccountCreationQuery accountCreationQuery =
                FinancialInstitutionAccountCreationQuery.builder()
                        .subType("checking")
                        .availableBalance(SampleHelper.generateRandomAmount())
                        .currentBalance(SampleHelper.generateRandomAmount())
                        .currency("EUR")
                        .description("Checking Account")
                        .reference(Iban.random(CountryCode.BE).toString())
                        .referenceType("IBAN")
                        .financialInstitutionId(financialInstitution.getId())
                        .financialInstitutionUserId(financialInstitutionUser.getId())
                        .build();

        return financialInstitutionAccountsService.create(accountCreationQuery);
    }

    public FinancialInstitutionAccount find(FinancialInstitution financialInstitution,
                                            FinancialInstitutionUser financialInstitutionUser,
                                            UUID financialInstitutionUserId) {
        FinancialInstitutionAccountReadQuery accountReadQuery =
                FinancialInstitutionAccountReadQuery.builder()
                        .financialInstitutionId(financialInstitution.getId())
                        .financialInstitutionUserId(financialInstitutionUser.getId())
                        .financialInstitutionAccountId(financialInstitutionUserId)
                        .build();

        return financialInstitutionAccountsService.find(accountReadQuery);
    }

    public void delete(FinancialInstitution financialInstitution,
                       FinancialInstitutionUser financialInstitutionUser,
                       FinancialInstitutionAccount financialInstitutionAccount){

        FinancialInstitutionAccountDeleteQuery accountDeleteQuery =
                FinancialInstitutionAccountDeleteQuery.builder()
                        .financialInstitutionId(financialInstitution.getId())
                        .financialInstitutionUserId(financialInstitutionUser.getId())
                        .financialInstitutionAccountId(financialInstitutionAccount.getId())
                        .build();

        financialInstitutionAccountsService.delete(accountDeleteQuery);
    }
}
