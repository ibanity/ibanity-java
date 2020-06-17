package com.ibanity.samples.sandbox;

import com.ibanity.apis.client.products.xs2a.models.FinancialInstitution;
import com.ibanity.apis.client.products.xs2a.sandbox.models.FinancialInstitutionAccount;
import com.ibanity.apis.client.products.xs2a.sandbox.models.FinancialInstitutionUser;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.create.FinancialInstitutionAccountCreationQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.delete.FinancialInstitutionAccountDeleteQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.read.FinancialInstitutionAccountReadQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.services.FinancialInstitutionAccountsService;
import com.ibanity.apis.client.services.IbanityService;
import com.ibanity.samples.helper.SampleHelper;
import org.iban4j.CountryCode;
import org.iban4j.Iban;

import static java.time.Instant.parse;
import java.util.UUID;

public class FinancialInstitutionAccountSample {
    private final FinancialInstitutionAccountsService financialInstitutionAccountsService;

    public FinancialInstitutionAccountSample(IbanityService ibanityService) {
        financialInstitutionAccountsService = ibanityService.xs2aService().sandbox().financialInstitutionAccountsService();
    }

    public FinancialInstitutionAccount create(FinancialInstitution financialInstitution, FinancialInstitutionUser financialInstitutionUser){
        FinancialInstitutionAccountCreationQuery accountCreationQuery =
                FinancialInstitutionAccountCreationQuery.builder()
                        .subtype("checking")
                        .availableBalance(SampleHelper.generateRandomAmount())
                        .currentBalance(SampleHelper.generateRandomAmount())
                        .currency("EUR")
                        .description("Checking Account")
                        .reference(Iban.random(CountryCode.BE).toString())
                        .referenceType("IBAN")
                        .product("Easy account")
                        .holderName("John Doe")
                        .currentBalanceChangedAt(parse("2018-10-25T00:00:00Z"))
                        .currentBalanceReferenceDate(parse("2018-10-25T01:00:00Z"))
                        .availableBalanceChangedAt(parse("2018-10-25T02:00:00Z"))
                        .availableBalanceReferenceDate(parse("2018-10-25T03:00:00Z"))
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

    public FinancialInstitutionAccount delete(FinancialInstitution financialInstitution,
                                              FinancialInstitutionUser financialInstitutionUser,
                                              FinancialInstitutionAccount financialInstitutionAccount){

        FinancialInstitutionAccountDeleteQuery accountDeleteQuery =
                FinancialInstitutionAccountDeleteQuery.builder()
                        .financialInstitutionId(financialInstitution.getId())
                        .financialInstitutionUserId(financialInstitutionUser.getId())
                        .financialInstitutionAccountId(financialInstitutionAccount.getId())
                        .build();

        return financialInstitutionAccountsService.delete(accountDeleteQuery);
    }
}
