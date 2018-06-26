package com.ibanity.apis.client.services.impl;

import com.ibanity.apis.client.exceptions.ResourceNotFoundException;
import com.ibanity.apis.client.models.Account;
import com.ibanity.apis.client.models.AccountInformationAccessAuthorization;
import com.ibanity.apis.client.models.AccountInformationAccessRequest;
import com.ibanity.apis.client.models.CustomerAccessToken;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.services.AccountsService;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import io.crnk.core.resource.list.ResourceList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

import static com.ibanity.apis.client.services.configuration.IbanityConfiguration.FORWARD_SLASH;

public class AccountsServiceImpl extends AbstractServiceImpl implements AccountsService {

    private static final Logger LOGGER = LogManager.getLogger(AccountsServiceImpl.class);

    private static final String ACCOUNTS_REQUEST_PATH                           = FORWARD_SLASH + "customer";
    private static final String ACCOUNTS_FI_REQUEST_PATH                        = ACCOUNTS_REQUEST_PATH + FORWARD_SLASH + FINANCIAL_INSTITUTIONS_PATH + FORWARD_SLASH + FINANCIAL_INSTITUTION_ID_TAG;
    private static final String ACCOUNT_INFORMATION_ACCESS_REQUEST_PATH         = ACCOUNTS_FI_REQUEST_PATH + FORWARD_SLASH + "account-information-access-requests" + FORWARD_SLASH + ACCOUNT_INFORMATION_ACCESS_REQUEST_ID_TAG;

    public AccountsServiceImpl() {
        super();
    }

    @Override
    public Account getCustomerAccount(final CustomerAccessToken customerAccessToken, final UUID accountId, final UUID financialInstitutionId) throws ResourceNotFoundException {
        String correctPath = ACCOUNTS_FI_REQUEST_PATH.replace(FINANCIAL_INSTITUTION_ID_TAG, financialInstitutionId.toString());

        ResourceRepositoryV2<Account, UUID> accountRepo = getApiClient(correctPath, customerAccessToken).getRepositoryForType(Account.class);
        QuerySpec querySpec = new QuerySpec(Account.class);
        try {
            return accountRepo.findOne(accountId, querySpec);
        } catch (io.crnk.core.exception.ResourceNotFoundException e) {
            String errorMessage = "Resource with ID:" + accountId + ": not found";
            LOGGER.debug(errorMessage);
            throw new ResourceNotFoundException(errorMessage);
        }
    }

    @Override
    public ResourceList<Account> getCustomerAccounts(final CustomerAccessToken customerAccessToken) {
        return getCustomerAccounts(customerAccessToken, new IbanityPagingSpec());
    }

    @Override
    public ResourceList<Account> getCustomerAccounts(final CustomerAccessToken customerAccessToken, final IbanityPagingSpec pagingSpec) {
        ResourceRepositoryV2<Account, UUID> accountsRepo = getApiClient(ACCOUNTS_REQUEST_PATH, customerAccessToken).getRepositoryForType(Account.class);
        QuerySpec querySpec = new QuerySpec(Account.class);
        querySpec.setPagingSpec(pagingSpec);
        return findAll(querySpec, accountsRepo);
    }

    @Override
    public ResourceList<Account> getCustomerAccounts(final CustomerAccessToken customerAccessToken, final UUID financialInstitutionId) throws ResourceNotFoundException {
        return getCustomerAccounts(customerAccessToken, financialInstitutionId, new IbanityPagingSpec());
    }

    @Override
    public ResourceList<Account> getCustomerAccounts(final CustomerAccessToken customerAccessToken, final UUID financialInstitutionId, final IbanityPagingSpec pagingSpec) throws ResourceNotFoundException {
        String correctPath = ACCOUNTS_FI_REQUEST_PATH.replace(FINANCIAL_INSTITUTION_ID_TAG, financialInstitutionId.toString());
        ResourceRepositoryV2<Account, UUID> accountsFinancialInstitutionRepo = getApiClient(correctPath, customerAccessToken).getRepositoryForType(Account.class);
        QuerySpec querySpec = new QuerySpec(Account.class);
        querySpec.setPagingSpec(pagingSpec);
        try {
            return findAll(querySpec, accountsFinancialInstitutionRepo);
        } catch (Exception e) {
            String errorMessage = "Resources with provided ID not found";
            LOGGER.debug(errorMessage);
            throw new ResourceNotFoundException(errorMessage, e);
        }
    }

    @Override
    public AccountInformationAccessRequest getAccountInformationAccessRequest(final CustomerAccessToken customerAccessToken, final AccountInformationAccessRequest accountInformationAccessRequest) {
        String correctPath = ACCOUNTS_FI_REQUEST_PATH.replace(FINANCIAL_INSTITUTION_ID_TAG, accountInformationAccessRequest.getFinancialInstitution().getId().toString());
        ResourceRepositoryV2<AccountInformationAccessRequest, UUID> accountInformationAccessRequestRepo = getApiClient(correctPath, customerAccessToken).getRepositoryForType(AccountInformationAccessRequest.class);
        return accountInformationAccessRequestRepo.create(accountInformationAccessRequest);
    }

    @Override
    public ResourceList<AccountInformationAccessAuthorization> getAccountsInformationAccessAuthorizations(final CustomerAccessToken customerAccessToken, final UUID financialInstitutionId, final UUID accountInformationAccessRequestId) throws ResourceNotFoundException {
        return getAccountsInformationAccessAuthorizations(customerAccessToken, financialInstitutionId, accountInformationAccessRequestId, new IbanityPagingSpec());
    }

    @Override
    public ResourceList<AccountInformationAccessAuthorization> getAccountsInformationAccessAuthorizations(final CustomerAccessToken customerAccessToken, final UUID financialInstitutionId, final UUID accountInformationAccessRequestId, final IbanityPagingSpec pagingSpec) throws ResourceNotFoundException {
        QuerySpec querySpec = new QuerySpec(AccountInformationAccessAuthorization.class);
        querySpec.setPagingSpec(pagingSpec);
        try {
            return findAll(querySpec, getAccountInformationAccessAuthorizationRepo(customerAccessToken, financialInstitutionId, accountInformationAccessRequestId));
        } catch (io.crnk.core.exception.ResourceNotFoundException e) {
            String errorMessage = "Resources with provided IDs not found";
            LOGGER.debug(errorMessage);
            throw new ResourceNotFoundException(errorMessage, e);
        }
    }

    @Override
    public void revokeAccountsAccessAuthorization(final CustomerAccessToken customerAccessToken, final UUID financialInstitutionId, final AccountInformationAccessAuthorization accountInformationAccessAuthorization) {
        getAccountInformationAccessAuthorizationRepo(customerAccessToken, financialInstitutionId, accountInformationAccessAuthorization.getAccountInformationAccessRequest().getId()).delete(accountInformationAccessAuthorization.getId());
    }

    private ResourceRepositoryV2<AccountInformationAccessAuthorization, UUID> getAccountInformationAccessAuthorizationRepo(final CustomerAccessToken customerAccessToken, final UUID financialInstitutionId, final UUID accountInformationAccessRequestId) {
        String correctPath = ACCOUNT_INFORMATION_ACCESS_REQUEST_PATH
                .replace(FINANCIAL_INSTITUTION_ID_TAG, financialInstitutionId.toString())
                .replace(ACCOUNT_INFORMATION_ACCESS_REQUEST_ID_TAG, accountInformationAccessRequestId.toString());
        return getApiClient(correctPath, customerAccessToken).getRepositoryForType(AccountInformationAccessAuthorization.class);

    }
}
