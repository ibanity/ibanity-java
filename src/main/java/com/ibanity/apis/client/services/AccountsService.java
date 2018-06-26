package com.ibanity.apis.client.services;

import com.ibanity.apis.client.exceptions.ResourceNotFoundException;
import com.ibanity.apis.client.models.Account;
import com.ibanity.apis.client.models.AccountInformationAccessAuthorization;
import com.ibanity.apis.client.models.AccountInformationAccessRequest;
import com.ibanity.apis.client.models.CustomerAccessToken;
import com.ibanity.apis.client.paging.IbanityPagingSpec;

import java.util.List;
import java.util.UUID;

public interface AccountsService {

    Account getCustomerAccount(CustomerAccessToken customerAccessToken, UUID accountId, UUID financialInstitutionId) throws ResourceNotFoundException;

    List<Account> getCustomerAccounts(CustomerAccessToken customerAccessToken);

    List<Account> getCustomerAccounts(CustomerAccessToken customerAccessToken, IbanityPagingSpec pagingSpec);

    List<Account> getCustomerAccounts(CustomerAccessToken customerAccessToken, UUID financialInstitutionId) throws ResourceNotFoundException;

    List<Account> getCustomerAccounts(CustomerAccessToken customerAccessToken, UUID financialInstitutionId, IbanityPagingSpec pagingSpec) throws ResourceNotFoundException;

    AccountInformationAccessRequest getAccountInformationAccessRequest(CustomerAccessToken customerAccessToken, AccountInformationAccessRequest accountInformationAccessRequest);

    List<AccountInformationAccessAuthorization> getAccountsInformationAccessAuthorizations(CustomerAccessToken customerAccessToken, UUID financialInstitutionId,  UUID accountInformationAccessRequestId) throws ResourceNotFoundException;

    List<AccountInformationAccessAuthorization> getAccountsInformationAccessAuthorizations(CustomerAccessToken customerAccessToken, UUID financialInstitutionId,  UUID accountInformationAccessRequestId, IbanityPagingSpec pagingSpec) throws ResourceNotFoundException;

    void revokeAccountsAccessAuthorization(CustomerAccessToken customerAccessToken, UUID financialInstitutionId, AccountInformationAccessAuthorization accountInformationAccessAuthorization) throws ResourceNotFoundException;
}
