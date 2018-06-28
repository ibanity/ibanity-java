package com.ibanity.apis.client.services;

import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.models.CustomerAccessToken;
import com.ibanity.apis.client.models.Transaction;
import com.ibanity.apis.client.paging.IbanityPagingSpec;

import java.util.List;
import java.util.UUID;

public interface TransactionsService {

    List<Transaction> getAccountTransactions(CustomerAccessToken customerAccessToken, UUID financialInstitutionId, UUID accountId) throws ApiErrorsException;

    List<Transaction> getAccountTransactions(CustomerAccessToken customerAccessToken, UUID financialInstitutionId, UUID accountId, IbanityPagingSpec pagingSpec) throws ApiErrorsException;

    Transaction getAccountTransaction(CustomerAccessToken customerAccessToken, UUID financialInstitutionId, UUID accountId, UUID transactionId) throws ApiErrorsException;
}
