package com.ibanity.apis.client.services;

import com.ibanity.apis.client.models.Transaction;
import com.ibanity.apis.client.models.factory.read.TransactionReadQuery;
import com.ibanity.apis.client.models.factory.read.TransactionsReadQuery;

import java.util.List;

public interface TransactionsService {

    List<Transaction> list(TransactionsReadQuery transactionsReadQuery);

    Transaction find(TransactionReadQuery transactionReadQuery);
}
