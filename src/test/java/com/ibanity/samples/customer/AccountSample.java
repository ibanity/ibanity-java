package com.ibanity.samples.customer;

import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.products.xs2a.models.Account;
import com.ibanity.apis.client.products.xs2a.models.CustomerAccessToken;
import com.ibanity.apis.client.products.xs2a.models.FinancialInstitution;
import com.ibanity.apis.client.products.xs2a.models.delete.AccountDeleteQuery;
import com.ibanity.apis.client.products.xs2a.models.read.AccountReadQuery;
import com.ibanity.apis.client.products.xs2a.models.read.AccountsReadQuery;
import com.ibanity.apis.client.products.xs2a.services.AccountsService;
import com.ibanity.apis.client.services.IbanityService;

import java.util.UUID;

public class AccountSample {

    private final AccountsService accountsService;

    public AccountSample(IbanityService ibanityService) {
        accountsService = ibanityService.xs2aService().accountsService();
    }

    public IbanityCollection<Account> list(CustomerAccessToken customerAccessToken, FinancialInstitution financialInstitution) {
        AccountsReadQuery accountsReadQuery = AccountsReadQuery.builder()
                .customerAccessToken(customerAccessToken.getToken())
                .financialInstitutionId(financialInstitution.getId())
                .build();

        return accountsService.list(accountsReadQuery);
    }

    public Account get(CustomerAccessToken customerAccessToken, FinancialInstitution financialInstitution, UUID accountId) {
        AccountReadQuery accountReadQuery = AccountReadQuery.builder()
                .customerAccessToken(customerAccessToken.getToken())
                .financialInstitutionId(financialInstitution.getId())
                .accountId(accountId)
                .build();

        return accountsService.find(accountReadQuery);
    }

    public Account delete(CustomerAccessToken customerAccessToken, FinancialInstitution financialInstitution, UUID accountId) {
        AccountDeleteQuery accountDeleteQuery = AccountDeleteQuery.builder()
                .customerAccessToken(customerAccessToken.getToken())
                .financialInstitutionId(financialInstitution.getId())
                .accountId(accountId)
                .build();

        return accountsService.delete(accountDeleteQuery);
    }
}
