package com.ibanity.apis.client.products.isabel_connect.services;

import com.ibanity.apis.client.models.IsabelCollection;
import com.ibanity.apis.client.products.isabel_connect.models.IntradayTransaction;
import com.ibanity.apis.client.products.isabel_connect.models.Transaction;
import com.ibanity.apis.client.products.isabel_connect.models.read.IntradayTransactionsReadQuery;

public interface IntradayTransactionService {
    IsabelCollection<IntradayTransaction> list(IntradayTransactionsReadQuery query);
}
