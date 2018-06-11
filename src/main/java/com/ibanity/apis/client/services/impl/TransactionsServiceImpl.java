package com.ibanity.apis.client.services.impl;

import com.ibanity.apis.client.exceptions.ResourceNotFoundException;
import com.ibanity.apis.client.models.Account;
import com.ibanity.apis.client.models.CustomerAccessToken;
import com.ibanity.apis.client.models.Transaction;
import com.ibanity.apis.client.models.sandbox.FinancialInstitutionTransaction;
import com.ibanity.apis.client.paging.PagingSpec;
import com.ibanity.apis.client.services.TransactionsService;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import io.crnk.core.resource.list.ResourceList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

public class TransactionsServiceImpl extends AbstractServiceImpl implements TransactionsService {
    private static final Logger LOGGER = LogManager.getLogger(TransactionsServiceImpl.class);

    private static final String TRANSACTIONS_REQUEST_PATH                   = "/customer/financial-institutions/"+FINANCIAL_INSTITUTION_ID_TAG+"/accounts/"+ACCOUNT_ID_TAG;

    private static final String SANDBOX_PATH                                = "/sandbox";
    private static final String SANDBOX_ACCOUNTS_FI_REQUEST_PATH            = SANDBOX_PATH+ "/"+FINANCIAL_INSTITUTIONS_PATH + "/"+FINANCIAL_INSTITUTION_ID_TAG;
    private static final String SANDBOX_USER_ACCOUNTS_FI_REQUEST_PATH       = SANDBOX_ACCOUNTS_FI_REQUEST_PATH+"/financial-institution-users/"+USER_ID_TAG;
    private static final String SANDBOX_ACCOUNTS_TXN_FI_REQUEST_PATH        = SANDBOX_USER_ACCOUNTS_FI_REQUEST_PATH+"/financial-institution-accounts/"+ACCOUNT_ID_TAG;

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

    @Override
    public FinancialInstitutionTransaction createSandBoxTransaction(CustomerAccessToken customerAccessToken, UUID financialInstitutionId, UUID financialInstitutionUserId, UUID sandboxAccountId, FinancialInstitutionTransaction sandboxTransaction) {
        String correctPath = SANDBOX_ACCOUNTS_TXN_FI_REQUEST_PATH
                .replace(FINANCIAL_INSTITUTION_ID_TAG, financialInstitutionId.toString())
                .replace(USER_ID_TAG, financialInstitutionUserId.toString())
                .replace(ACCOUNT_ID_TAG, sandboxAccountId.toString())
                ;
        ResourceRepositoryV2<FinancialInstitutionTransaction, UUID> transactionsRepo = getApiClient(correctPath, customerAccessToken).getRepositoryForType(FinancialInstitutionTransaction.class);
        FinancialInstitutionTransaction createdTransaction = transactionsRepo.create(sandboxTransaction);

        return createdTransaction;
    }

    @Override
    public void deleteSandboxFinancialInstitutionTransaction(CustomerAccessToken customerAccessToken, UUID financialInstitutionId, UUID financialInstitutionUserId, UUID sandboxAccountId, UUID sandboxTransactionId) throws ResourceNotFoundException {
        try {
            String correctPath = SANDBOX_ACCOUNTS_TXN_FI_REQUEST_PATH
                    .replace(FINANCIAL_INSTITUTION_ID_TAG, financialInstitutionId.toString())
                    .replace(USER_ID_TAG, financialInstitutionUserId.toString())
                    .replace(ACCOUNT_ID_TAG, sandboxAccountId.toString())
                    ;
            ResourceRepositoryV2<FinancialInstitutionTransaction, UUID> transactionsRepo = getApiClient(correctPath, customerAccessToken).getRepositoryForType(FinancialInstitutionTransaction.class);
            transactionsRepo.delete(sandboxTransactionId);
        } catch (io.crnk.core.exception.ResourceNotFoundException e) {
            String errorMessage = "Resource with ID:"+sandboxTransactionId+": not found";
            LOGGER.debug(errorMessage);
            throw new ResourceNotFoundException(errorMessage);
        }
    }

    private ResourceRepositoryV2<Transaction, UUID> getRepository(CustomerAccessToken customerAccessToken, Account account){
        String correctPath = TRANSACTIONS_REQUEST_PATH
                .replace(FINANCIAL_INSTITUTION_ID_TAG, account.getFinancialInstitution().getId().toString())
                .replace(ACCOUNT_ID_TAG, account.getId().toString());
        return getApiClient(correctPath, customerAccessToken).getRepositoryForType(Transaction.class);
    }
}
