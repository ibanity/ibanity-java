package com.ibanity.apis.client.services;

import com.ibanity.apis.client.exceptions.ResourceNotFoundException;
import com.ibanity.apis.client.models.CustomerAccessToken;
import com.ibanity.apis.client.models.Transaction;
import com.ibanity.apis.client.paging.IbanityPagingSpec;

import java.util.List;
import java.util.UUID;

public interface TransactionsService {

    List<Transaction> getAccountTransactions(CustomerAccessToken customerAccessToken, UUID financialInstitutionId, UUID accountId) throws ResourceNotFoundException;

    List<Transaction> getAccountTransactions(CustomerAccessToken customerAccessToken, UUID financialInstitutionId, UUID accountId, IbanityPagingSpec pagingSpec) throws ResourceNotFoundException;

    Transaction getAccountTransaction(CustomerAccessToken customerAccessToken, UUID financialInstitutionId, UUID accountId, UUID transactionId) throws ResourceNotFoundException;
}
