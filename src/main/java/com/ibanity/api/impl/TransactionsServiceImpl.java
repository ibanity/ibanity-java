package com.ibanity.api.impl;

import com.ibanity.api.TransactionsService;
import com.ibanity.models.Account;
import com.ibanity.models.CustomerAccessToken;
import com.ibanity.models.Transaction;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.UUID;

public class TransactionsServiceImpl extends AbstractServiceImpl implements TransactionsService {
    private static final Logger LOGGER = LogManager.getLogger(TransactionsServiceImpl.class);
    private static final String TRANSACTIONS_REQUEST_PATH = "/customer/financial-institutions/"+FINANCIAL_INSTITUTION_ID_TAG+"/accounts/"+ACCOUNT_ID_TAG;

    public TransactionsServiceImpl() {
        super();
    }

    @Override
    public List<Transaction> getAccountTransactions(CustomerAccessToken customerAccessToken, Account account) {
        return getRepository(customerAccessToken, account).findAll(new QuerySpec(Transaction.class));
    }

    @Override
    public Transaction getAccountTransaction(CustomerAccessToken customerAccessToken, Account account, UUID transactionId) {
        return getRepository(customerAccessToken, account).findOne(transactionId, new QuerySpec(Transaction.class));
    }

    private ResourceRepositoryV2<Transaction, UUID> getRepository(CustomerAccessToken customerAccessToken, Account account){
        String correctPath = TRANSACTIONS_REQUEST_PATH
                .replace(FINANCIAL_INSTITUTION_ID_TAG, account.getFinancialInstitution().getId().toString())
                .replace(ACCOUNT_ID_TAG, account.getId().toString());
        ResourceRepositoryV2<Transaction, UUID> transactionsRepo = getApiClient(correctPath, customerAccessToken).getRepositoryForType(Transaction.class);
        return getApiClient(correctPath, customerAccessToken).getRepositoryForType(Transaction.class);
    }
}
