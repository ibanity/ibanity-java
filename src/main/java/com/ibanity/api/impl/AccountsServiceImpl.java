package com.ibanity.api.impl;

import com.ibanity.api.AccountsService;
import com.ibanity.models.Account;
import com.ibanity.models.AccountInformationAccessAuthorization;
import com.ibanity.models.AccountInformationAccessRequest;
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
    private static final String ACCOUNTS_FI_REQUEST_PATH = ACCOUNTS_REQUEST_PATH+ "/financial-institutions/"+FINANCIAL_INSTITUTION_ID_TAG;
    private static final String ACCOUNT_INFORMATION_ACCESS_REQUEST_PATH = ACCOUNTS_FI_REQUEST_PATH + "/account-information-access-requests"+ACCOUNT_INFORMATION_ACCESS_REQUEST_ID_TAG;
    private static final String ACCOUNT_INFORMATION_ACCESS_AUTHORIZATION_PATH = ACCOUNT_INFORMATION_ACCESS_REQUEST_PATH + "/account-information-access-authorizations";

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
        return accountsFinancialInstitutionRepo.findAll(new QuerySpec(Account.class));
    }


    @Override
    public AccountInformationAccessRequest getAccountInformationAccessRedirectUrl(CustomerAccessToken customerAccessToken, UUID financialInstitutionId, AccountInformationAccessRequest accountInformationAccessRequest) {
        String correctPath = ACCOUNTS_FI_REQUEST_PATH.replace(FINANCIAL_INSTITUTION_ID_TAG, financialInstitutionId.toString());
        ResourceRepositoryV2<AccountInformationAccessRequest, UUID> accountInformationAccessRequestRepo = getApiClient(correctPath, customerAccessToken).getRepositoryForType(AccountInformationAccessRequest.class);
        return accountInformationAccessRequestRepo.create(accountInformationAccessRequest);
    }

    @Override
    public List<AccountInformationAccessAuthorization> getAccountInformationAccessAuthorizations(CustomerAccessToken customerAccessToken, UUID financialInstitutionId, UUID accountInformationAccessRequestId) {
        return getAccountInformationAccessAuthorizationRepo(customerAccessToken, financialInstitutionId, accountInformationAccessRequestId).findAll(new QuerySpec(AccountInformationAccessAuthorization.class));
    }

    @Override
    public void removeAccountAccess(CustomerAccessToken customerAccessToken, UUID financialInstitutionId, UUID accountInformationAccessRequestId, UUID accountInformationAccessAuthorizationId) {
        getAccountInformationAccessAuthorizationRepo(customerAccessToken, financialInstitutionId, accountInformationAccessRequestId).delete(accountInformationAccessAuthorizationId);
    }

    private ResourceRepositoryV2<AccountInformationAccessAuthorization, UUID> getAccountInformationAccessAuthorizationRepo(CustomerAccessToken customerAccessToken, UUID financialInstitutionId, UUID accountInformationAccessRequestId) {
        String correctPath = ACCOUNT_INFORMATION_ACCESS_AUTHORIZATION_PATH
                .replace(FINANCIAL_INSTITUTION_ID_TAG, financialInstitutionId.toString())
                .replace(ACCOUNT_INFORMATION_ACCESS_REQUEST_ID_TAG, accountInformationAccessRequestId.toString());
        return getApiClient(correctPath, customerAccessToken).getRepositoryForType(AccountInformationAccessAuthorization.class);

    }
}
