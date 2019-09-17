package com.ibanity.apis.client.products.ponto_connect.services;

import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.products.ponto_connect.models.Transaction;
import com.ibanity.apis.client.products.ponto_connect.models.read.TransactionReadQuery;
import com.ibanity.apis.client.products.ponto_connect.models.read.TransactionsReadQuery;

public interface TransactionService {

    Transaction find(TransactionReadQuery transactionReadQuery);

    IbanityCollection<Transaction> list(TransactionsReadQuery transactionsReadQuery);
}
