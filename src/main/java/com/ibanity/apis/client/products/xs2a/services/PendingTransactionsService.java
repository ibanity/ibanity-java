package com.ibanity.apis.client.products.xs2a.services;

import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.products.xs2a.models.PendingTransaction;
import com.ibanity.apis.client.products.xs2a.models.read.PendingTransactionReadQuery;
import com.ibanity.apis.client.products.xs2a.models.read.PendingTransactionsReadQuery;

public interface PendingTransactionsService {

    IbanityCollection<PendingTransaction> list(PendingTransactionsReadQuery pendingTransactionsReadQuery);

    IbanityCollection<PendingTransaction> listUpdatedForSynchronization(PendingTransactionsReadQuery pendingTransactionsReadQuery);

    PendingTransaction find(PendingTransactionReadQuery pendingTransactionReadQuery);
}
