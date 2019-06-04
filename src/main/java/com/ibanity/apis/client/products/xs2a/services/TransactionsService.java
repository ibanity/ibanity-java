package com.ibanity.apis.client.products.xs2a.services;

import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.products.xs2a.models.Transaction;
import com.ibanity.apis.client.products.xs2a.models.read.TransactionReadQuery;
import com.ibanity.apis.client.products.xs2a.models.read.TransactionsReadQuery;

public interface TransactionsService {

    IbanityCollection<Transaction> list(TransactionsReadQuery transactionsReadQuery);

    Transaction find(TransactionReadQuery transactionReadQuery);
}
