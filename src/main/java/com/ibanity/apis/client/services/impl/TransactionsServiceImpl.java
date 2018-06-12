package com.ibanity.apis.client.services.impl;

import com.ibanity.apis.client.exceptions.ResourceNotFoundException;
import com.ibanity.apis.client.models.Account;
import com.ibanity.apis.client.models.CustomerAccessToken;
import com.ibanity.apis.client.models.Transaction;
import com.ibanity.apis.client.models.sandbox.FinancialInstitutionTransaction;
import com.ibanity.apis.client.paging.IBanityPagingSpec;
import com.ibanity.apis.client.services.TransactionsService;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import io.crnk.core.resource.list.ResourceList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

import static com.ibanity.apis.client.services.configuration.IBanityConfiguration.FORWARD_SLASH;
import static com.ibanity.apis.client.services.configuration.IBanityConfiguration.SANBOX_PREFIX_PATH;

public class TransactionsServiceImpl extends AbstractServiceImpl implements TransactionsService {
    private static final Logger LOGGER = LogManager.getLogger(TransactionsServiceImpl.class);

    private static final String TRANSACTIONS_REQUEST_PATH                   = FORWARD_SLASH + "customer" + FORWARD_SLASH + "financial-institutions" + FORWARD_SLASH + FINANCIAL_INSTITUTION_ID_TAG + FORWARD_SLASH + "accounts" + FORWARD_SLASH + ACCOUNT_ID_TAG;

    private static final String SANDBOX_ACCOUNTS_FI_REQUEST_PATH            = SANBOX_PREFIX_PATH + FORWARD_SLASH + FINANCIAL_INSTITUTIONS_PATH + FORWARD_SLASH + FINANCIAL_INSTITUTION_ID_TAG;
    private static final String SANDBOX_USER_ACCOUNTS_FI_REQUEST_PATH       = SANDBOX_ACCOUNTS_FI_REQUEST_PATH + FORWARD_SLASH + "financial-institution-users" + FORWARD_SLASH + USER_ID_TAG;
    private static final String SANDBOX_ACCOUNTS_TXN_FI_REQUEST_PATH        = SANDBOX_USER_ACCOUNTS_FI_REQUEST_PATH + FORWARD_SLASH + "financial-institution-accounts" + FORWARD_SLASH + ACCOUNT_ID_TAG;

    public TransactionsServiceImpl() {
        super();
    }

    @Override
    public ResourceList<Transaction> getAccountTransactions(CustomerAccessToken customerAccessToken, Account account) {
        return getAccountTransactions(customerAccessToken, account, new IBanityPagingSpec());
    }

    @Override
    public ResourceList<Transaction> getAccountTransactions(CustomerAccessToken customerAccessToken, Account account, IBanityPagingSpec pagingSpec) {
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
        return transactionsRepo.create(sandboxTransaction);
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
