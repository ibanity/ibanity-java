package com.ibanity.apis.client.services.impl;

import com.ibanity.apis.client.services.TransactionsService;
import com.ibanity.apis.client.models.Account;
import com.ibanity.apis.client.models.CustomerAccessToken;
import com.ibanity.apis.client.models.Transaction;
import com.ibanity.apis.client.paging.PagingSpec;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import io.crnk.core.resource.list.ResourceList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

public class TransactionsServiceImpl extends AbstractServiceImpl implements TransactionsService {
    private static final Logger LOGGER = LogManager.getLogger(TransactionsServiceImpl.class);
    private static final String TRANSACTIONS_REQUEST_PATH = "/customer/financial-institutions/"+FINANCIAL_INSTITUTION_ID_TAG+"/accounts/"+ACCOUNT_ID_TAG;

    public TransactionsServiceImpl() {
        super();
    }

    @Override
    public ResourceList<Transaction> getAccountTransactions(CustomerAccessToken customerAccessToken, Account account) {
        return getAccountTransactions(customerAccessToken, account, new PagingSpec());
    }

    @Override
    public ResourceList<Transaction> getAccountTransactions(CustomerAccessToken customerAccessToken, Account account, PagingSpec pagingSpec) {
        QuerySpec querySpec = new QuerySpec(Transaction.class);
        querySpec.setPagingSpec(pagingSpec);
        return findAll(querySpec, getRepository(customerAccessToken, account));

    }

    @Override
    public Transaction getAccountTransaction(CustomerAccessToken customerAccessToken, Account account, UUID transactionId) {
        return getRepository(customerAccessToken, account).findOne(transactionId, new QuerySpec(Transaction.class));
    }

    private ResourceRepositoryV2<Transaction, UUID> getRepository(CustomerAccessToken customerAccessToken, Account account){
        String correctPath = TRANSACTIONS_REQUEST_PATH
                .replace(FINANCIAL_INSTITUTION_ID_TAG, account.getFinancialInstitution().getId().toString())
                .replace(ACCOUNT_ID_TAG, account.getId().toString());
        return getApiClient(correctPath, customerAccessToken).getRepositoryForType(Transaction.class);
    }
}
