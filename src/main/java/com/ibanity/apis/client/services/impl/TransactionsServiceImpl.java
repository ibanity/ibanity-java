package com.ibanity.apis.client.services.impl;

import com.ibanity.apis.client.models.Account;
import com.ibanity.apis.client.models.CustomerAccessToken;
import com.ibanity.apis.client.models.Transaction;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.services.TransactionsService;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import io.crnk.core.resource.list.ResourceList;

import java.util.UUID;

import static com.ibanity.apis.client.services.configuration.IbanityConfiguration.FORWARD_SLASH;

public class TransactionsServiceImpl extends AbstractServiceImpl implements TransactionsService {

    private static final String TRANSACTIONS_REQUEST_PATH = FORWARD_SLASH + "customer" + FORWARD_SLASH + "financial-institutions" + FORWARD_SLASH + FINANCIAL_INSTITUTION_ID_TAG + FORWARD_SLASH + "accounts" + FORWARD_SLASH + ACCOUNT_ID_TAG;

    public TransactionsServiceImpl() {
        super();
    }

    @Override
    public ResourceList<Transaction> getAccountTransactions(CustomerAccessToken customerAccessToken, Account account) {
        return getAccountTransactions(customerAccessToken, account, new IbanityPagingSpec());
    }

    @Override
    public ResourceList<Transaction> getAccountTransactions(CustomerAccessToken customerAccessToken, Account account, IbanityPagingSpec pagingSpec) {
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
