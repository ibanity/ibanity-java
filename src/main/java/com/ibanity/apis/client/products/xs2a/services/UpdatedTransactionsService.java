package com.ibanity.apis.client.products.xs2a.services;

import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.products.xs2a.models.Transaction;
import com.ibanity.apis.client.products.xs2a.models.read.UpdatedTransactionsReadQuery;

public interface UpdatedTransactionsService {

    IbanityCollection<Transaction> list(UpdatedTransactionsReadQuery updatedTransactionsReadQuery);
}
