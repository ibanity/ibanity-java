package com.ibanity.apis.client.products.xs2a.services;

import com.ibanity.apis.client.products.xs2a.models.TransactionDeleteRequest;
import com.ibanity.apis.client.products.xs2a.models.create.TransactionDeleteRequestCreationQuery;

public interface TransactionDeleteRequestsService {

    TransactionDeleteRequest createForApplication(TransactionDeleteRequestCreationQuery transactionDeleteRequestCreationQuery);

    TransactionDeleteRequest createForCustomer(TransactionDeleteRequestCreationQuery transactionDeleteRequestCreationQuery);

    TransactionDeleteRequest createForAccount(TransactionDeleteRequestCreationQuery transactionDeleteRequestCreationQuery);
}
