package com.ibanity.apis.client.services;

import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.models.Transaction;
import com.ibanity.apis.client.models.factory.read.TransactionReadQuery;
import com.ibanity.apis.client.models.factory.read.TransactionsReadQuery;

public interface TransactionsService {

    IbanityCollection<Transaction> list(TransactionsReadQuery transactionsReadQuery);

    Transaction find(TransactionReadQuery transactionReadQuery);
}
