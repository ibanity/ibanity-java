package com.ibanity.apis.client.products.ponto_connect.services;

import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.products.ponto_connect.models.PendingTransaction;
import com.ibanity.apis.client.products.ponto_connect.models.read.PendingTransactionReadQuery;
import com.ibanity.apis.client.products.ponto_connect.models.read.PendingTransactionsReadQuery;

public interface PendingTransactionService {

    PendingTransaction find(PendingTransactionReadQuery pendingTransactionReadQuery);

    IbanityCollection<PendingTransaction> list(PendingTransactionsReadQuery pendingTransactionsReadQuery);

    IbanityCollection<PendingTransaction> listUpdatedForSynchronization(PendingTransactionsReadQuery pendingTransactionsReadQuery);
}
