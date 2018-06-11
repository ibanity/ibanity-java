package com.ibanity.apis.client.services.impl;

import com.ibanity.apis.client.exceptions.ResourceNotFoundException;
import com.ibanity.apis.client.models.Account;
import com.ibanity.apis.client.models.AccountInformationAccessAuthorization;
import com.ibanity.apis.client.models.AccountInformationAccessRequest;
import com.ibanity.apis.client.models.CustomerAccessToken;
import com.ibanity.apis.client.models.sandbox.FinancialInstitutionAccount;
import com.ibanity.apis.client.paging.PagingSpec;
import com.ibanity.apis.client.services.AccountsService;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import io.crnk.core.resource.list.ResourceList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

public class AccountsServiceImpl extends AbstractServiceImpl implements AccountsService {

    private static final Logger LOGGER = LogManager.getLogger(AccountsServiceImpl.class);

    private static final String ACCOUNTS_REQUEST_PATH                           = "/customer";
    private static final String ACCOUNT_REQUEST_PATH                            = "/accounts";
    private static final String ACCOUNTS_FI_REQUEST_PATH                        = ACCOUNTS_REQUEST_PATH + "/"+FINANCIAL_INSTITUTIONS_PATH + "/"+ FINANCIAL_INSTITUTION_ID_TAG;
    private static final String ACCOUNT_INFORMATION_ACCESS_REQUEST_PATH         = ACCOUNTS_FI_REQUEST_PATH + "/account-information-access-requests/"+ACCOUNT_INFORMATION_ACCESS_REQUEST_ID_TAG;

    private static final String SANDBOX_PATH                                    = "/sandbox";
    private static final String SANDBOX_ACCOUNTS_FI_REQUEST_PATH                = SANDBOX_PATH+ "/"+FINANCIAL_INSTITUTIONS_PATH + "/"+FINANCIAL_INSTITUTION_ID_TAG;
    private static final String SANDBOX_USER_ACCOUNTS_FI_REQUEST_PATH           = SANDBOX_ACCOUNTS_FI_REQUEST_PATH+"/financial-institution-users/"+USER_ID_TAG;

    public AccountsServiceImpl() {
        super();
    }

    @Override
    public Account getCustomerAccount(CustomerAccessToken customerAccessToken, UUID accountId, UUID financialInstitutionId) throws ResourceNotFoundException {
        String correctPath = ACCOUNTS_FI_REQUEST_PATH.replace(FINANCIAL_INSTITUTION_ID_TAG, financialInstitutionId.toString());

        ResourceRepositoryV2<Account, UUID> accountRepo = getApiClient(correctPath, customerAccessToken).getRepositoryForType(Account.class);
        QuerySpec querySpec = new QuerySpec(Account.class);
        try {
            return accountRepo.findOne(accountId, querySpec);
        } catch (io.crnk.core.exception.ResourceNotFoundException e) {
            String errorMessage = "Resource with ID:"+accountId+": not found";
            LOGGER.debug(errorMessage);
            throw new ResourceNotFoundException(errorMessage);
        }
    }

    @Override
    public ResourceList<Account> getCustomerAccounts(CustomerAccessToken customerAccessToken) {
        return getCustomerAccounts(customerAccessToken, new PagingSpec());
    }

    @Override
    public ResourceList<Account> getCustomerAccounts(CustomerAccessToken customerAccessToken, PagingSpec pagingSpec) {
        ResourceRepositoryV2<Account, UUID> accountsRepo = getApiClient(ACCOUNTS_REQUEST_PATH, customerAccessToken).getRepositoryForType(Account.class);
        QuerySpec querySpec = new QuerySpec(Account.class);
        querySpec.setPagingSpec(pagingSpec);
        return findAll(querySpec, accountsRepo);
    }

    @Override
    public ResourceList<Account> getCustomerAccounts(CustomerAccessToken customerAccessToken, UUID financialInstitutionId) {
        return getCustomerAccounts(customerAccessToken, financialInstitutionId, new PagingSpec());
    }

    @Override
    public ResourceList<Account> getCustomerAccounts(CustomerAccessToken customerAccessToken, UUID financialInstitutionId, PagingSpec pagingSpec) {
        String correctPath = ACCOUNTS_FI_REQUEST_PATH.replace(FINANCIAL_INSTITUTION_ID_TAG, financialInstitutionId.toString());
        ResourceRepositoryV2<Account, UUID> accountsFinancialInstitutionRepo = getApiClient(correctPath, customerAccessToken).getRepositoryForType(Account.class);
        QuerySpec querySpec = new QuerySpec(Account.class);
        querySpec.setPagingSpec(pagingSpec);
        return findAll(querySpec, accountsFinancialInstitutionRepo);
    }

    @Override
    public AccountInformationAccessRequest getAccountsInformationAccessRedirectUrl(CustomerAccessToken customerAccessToken, AccountInformationAccessRequest accountInformationAccessRequest) {
        String correctPath = ACCOUNTS_FI_REQUEST_PATH.replace(FINANCIAL_INSTITUTION_ID_TAG, accountInformationAccessRequest.getFinancialInstitution().getId().toString());
        ResourceRepositoryV2<AccountInformationAccessRequest, UUID> accountInformationAccessRequestRepo = getApiClient(correctPath, customerAccessToken).getRepositoryForType(AccountInformationAccessRequest.class);
        return accountInformationAccessRequestRepo.create(accountInformationAccessRequest);
    }

    @Override
    public ResourceList<AccountInformationAccessAuthorization> getAccountsInformationAccessAuthorizations(CustomerAccessToken customerAccessToken, AccountInformationAccessRequest accountInformationAccessRequest) {
        return getAccountsInformationAccessAuthorizations(customerAccessToken, accountInformationAccessRequest, new PagingSpec());
    }

    @Override
    public ResourceList<AccountInformationAccessAuthorization> getAccountsInformationAccessAuthorizations(CustomerAccessToken customerAccessToken, AccountInformationAccessRequest accountInformationAccessRequest, PagingSpec pagingSpec) {
        QuerySpec querySpec = new QuerySpec(AccountInformationAccessAuthorization.class);
        querySpec.setPagingSpec(pagingSpec);
        return findAll(querySpec, getAccountInformationAccessAuthorizationRepo(customerAccessToken, accountInformationAccessRequest.getFinancialInstitution().getId(), accountInformationAccessRequest.getId()));
    }

    @Override
    public void revokeAccountsAccessAuthorization(CustomerAccessToken customerAccessToken, UUID financialInstitutionId, AccountInformationAccessAuthorization accountInformationAccessAuthorization) {
        getAccountInformationAccessAuthorizationRepo(customerAccessToken, financialInstitutionId, accountInformationAccessAuthorization.getAccountInformationAccessRequest().getId()).delete(accountInformationAccessAuthorization.getId());
    }

    @Override
    public FinancialInstitutionAccount createSandBoxAccount(CustomerAccessToken customerAccessToken, UUID financialInstitutionId, UUID financialInstitutionUserId, FinancialInstitutionAccount sandboxAccount) {
        String correctPath = SANDBOX_USER_ACCOUNTS_FI_REQUEST_PATH
                .replace(FINANCIAL_INSTITUTION_ID_TAG, financialInstitutionId.toString())
                .replace(USER_ID_TAG, financialInstitutionUserId.toString())
                ;
        ResourceRepositoryV2<FinancialInstitutionAccount, UUID> accountsRepo = getApiClient(correctPath, customerAccessToken).getRepositoryForType(FinancialInstitutionAccount.class);
        FinancialInstitutionAccount createdAccount = accountsRepo.create(sandboxAccount);

        return createdAccount;
    }

    @Override
    public void deleteSandBoxAccount(CustomerAccessToken customerAccessToken, UUID financialInstitutionId, UUID financialInstitutionUserId, UUID sandboxAccountId) {
        String correctPath = SANDBOX_USER_ACCOUNTS_FI_REQUEST_PATH
                .replace(FINANCIAL_INSTITUTION_ID_TAG, financialInstitutionId.toString())
                .replace(USER_ID_TAG, financialInstitutionUserId.toString())
                ;
        ResourceRepositoryV2<FinancialInstitutionAccount, UUID> accountsRepo = getApiClient(correctPath, customerAccessToken).getRepositoryForType(FinancialInstitutionAccount.class);
        accountsRepo.delete(sandboxAccountId);
    }

    private ResourceRepositoryV2<AccountInformationAccessAuthorization, UUID> getAccountInformationAccessAuthorizationRepo(CustomerAccessToken customerAccessToken, UUID financialInstitutionId, UUID accountInformationAccessRequestId) {
        String correctPath = ACCOUNT_INFORMATION_ACCESS_REQUEST_PATH
                .replace(FINANCIAL_INSTITUTION_ID_TAG, financialInstitutionId.toString())
                .replace(ACCOUNT_INFORMATION_ACCESS_REQUEST_ID_TAG, accountInformationAccessRequestId.toString());
        return getApiClient(correctPath, customerAccessToken).getRepositoryForType(AccountInformationAccessAuthorization.class);

    }
}
