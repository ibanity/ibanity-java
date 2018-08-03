package com.ibanity.apis.client.services;

import com.ibanity.apis.client.models.Transaction;
import com.ibanity.apis.client.paging.IbanityPagingSpec;

import java.util.List;
import java.util.UUID;

public interface TransactionsService {

    List<Transaction> list(String customerAccessToken, UUID financialInstitutionId, UUID accountId);

    List<Transaction> list(String customerAccessToken, UUID financialInstitutionId, UUID accountId, IbanityPagingSpec pagingSpec);

    Transaction find(String customerAccessToken, UUID financialInstitutionId, UUID accountId, UUID transactionId);
}
