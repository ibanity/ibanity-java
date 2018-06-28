package com.ibanity.apis.client.services;

import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.models.Account;
import com.ibanity.apis.client.models.AccountInformationAccessRequest;
import com.ibanity.apis.client.models.CustomerAccessToken;
import com.ibanity.apis.client.paging.IbanityPagingSpec;

import java.util.List;
import java.util.UUID;

public interface AccountsService {

    Account getCustomerAccount(CustomerAccessToken customerAccessToken, UUID accountId, UUID financialInstitutionId) throws ApiErrorsException;

    List<Account> getCustomerAccounts(CustomerAccessToken customerAccessToken);

    List<Account> getCustomerAccounts(CustomerAccessToken customerAccessToken, IbanityPagingSpec pagingSpec);

    List<Account> getCustomerAccounts(CustomerAccessToken customerAccessToken, UUID financialInstitutionId) throws ApiErrorsException;

    List<Account> getCustomerAccounts(CustomerAccessToken customerAccessToken, UUID financialInstitutionId, IbanityPagingSpec pagingSpec) throws ApiErrorsException;

    AccountInformationAccessRequest getAccountInformationAccessRequest(CustomerAccessToken customerAccessToken, AccountInformationAccessRequest accountInformationAccessRequest);
}
