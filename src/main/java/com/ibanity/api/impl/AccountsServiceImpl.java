package com.ibanity.api.impl;

import com.ibanity.api.AccountsService;
import com.ibanity.models.Account;
import com.ibanity.models.CustomerAccessToken;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.UUID;

public class AccountsServiceImpl extends AbstractServiceImpl implements AccountsService {

    private static final Logger LOGGER = LogManager.getLogger(AccountsServiceImpl.class);

    private static final String ACCOUNTS_REQUEST_PATH = "/customer";
    private static final String ACCOUNTS_FI_REQUEST_PATH = "/customer/financial-institutions/"+FINANCIAL_INSTITUTION_ID_TAG;

    public AccountsServiceImpl() {
        super();
    }

    @Override
    public List<Account> getCustomerAccounts(CustomerAccessToken customerAccessToken) {
        ResourceRepositoryV2<Account, UUID> accountsRepo = getApiClient(ACCOUNTS_REQUEST_PATH, customerAccessToken).getRepositoryForType(Account.class);
        return accountsRepo.findAll(new QuerySpec(Account.class));
    }

    @Override
    public List<Account> getCustomerAccounts(CustomerAccessToken customerAccessToken, UUID financialInstitutionId) {
        String correctPath = ACCOUNTS_FI_REQUEST_PATH.replace(FINANCIAL_INSTITUTION_ID_TAG, financialInstitutionId.toString());
        ResourceRepositoryV2<Account, UUID> accountsFinancialInstitutionRepo = getApiClient(correctPath, customerAccessToken).getRepositoryForType(Account.class);
        List<Account> result = accountsFinancialInstitutionRepo.findAll(new QuerySpec(Account.class));
        return result;
    }
}
