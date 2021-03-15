package com.ibanity.apis.client.products.isabel_connect.services;

import com.ibanity.apis.client.models.IsabelCollection;
import com.ibanity.apis.client.products.isabel_connect.models.Transaction;
import com.ibanity.apis.client.products.isabel_connect.models.read.TransactionsReadQuery;

public interface TransactionService {
    IsabelCollection<Transaction> list(TransactionsReadQuery query);
}
