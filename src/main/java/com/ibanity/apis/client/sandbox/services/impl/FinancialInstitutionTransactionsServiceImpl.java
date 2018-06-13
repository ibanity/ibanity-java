package com.ibanity.apis.client.sandbox.services.impl;

import com.ibanity.apis.client.exceptions.ResourceNotFoundException;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionTransaction;
import com.ibanity.apis.client.sandbox.services.FinancialInstitutionTransactionsService;
import com.ibanity.apis.client.services.impl.AbstractServiceImpl;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.UUID;

import static com.ibanity.apis.client.services.configuration.IbanityConfiguration.FORWARD_SLASH;
import static com.ibanity.apis.client.services.configuration.IbanityConfiguration.SANBOX_PREFIX_PATH;

public class FinancialInstitutionTransactionsServiceImpl extends AbstractServiceImpl implements FinancialInstitutionTransactionsService {
    private static final String SANDBOX_ACCOUNTS_FI_REQUEST_PATH            = SANBOX_PREFIX_PATH + FORWARD_SLASH + FINANCIAL_INSTITUTIONS_PATH + FORWARD_SLASH + FINANCIAL_INSTITUTION_ID_TAG;
    private static final String SANDBOX_USER_ACCOUNTS_FI_REQUEST_PATH       = SANDBOX_ACCOUNTS_FI_REQUEST_PATH + FORWARD_SLASH + "financial-institution-users" + FORWARD_SLASH + USER_ID_TAG;
    private static final String SANDBOX_ACCOUNTS_TXN_FI_REQUEST_PATH        = SANDBOX_USER_ACCOUNTS_FI_REQUEST_PATH + FORWARD_SLASH + "financial-institution-accounts" + FORWARD_SLASH + ACCOUNT_ID_TAG;


    private static final Logger LOGGER = LogManager.getLogger(FinancialInstitutionTransactionsServiceImpl.class);

    @Override
    public FinancialInstitutionTransaction getFinancialInstitutionTransaction(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId, UUID financialInstitutionTransactionId) throws ResourceNotFoundException {
        ResourceRepositoryV2<FinancialInstitutionTransaction, UUID> transactionsRepo = getTransactionsRepo(financialInstitutionId, financialInstitutionUserId, financialInstitutionAccountId);
        QuerySpec querySpec = new QuerySpec(FinancialInstitutionTransaction.class);
        try {
            return transactionsRepo.findOne(financialInstitutionTransactionId, querySpec);
        } catch (io.crnk.core.exception.ResourceNotFoundException e) {
            String errorMessage = "Resource with provided IDs not found";
            LOGGER.debug(errorMessage);
            throw new ResourceNotFoundException(errorMessage);
        }
    }

    @Override
    public List<FinancialInstitutionTransaction> getFinancialInstitutionAccountTransactions(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId) throws ResourceNotFoundException{
        ResourceRepositoryV2<FinancialInstitutionTransaction, UUID> transactionsRepo = getTransactionsRepo(financialInstitutionId, financialInstitutionUserId, financialInstitutionAccountId);
        QuerySpec querySpec = new QuerySpec(FinancialInstitutionTransaction.class);
        try {
            return transactionsRepo.findAll(querySpec);
        } catch (io.crnk.core.exception.ResourceNotFoundException e) {
            String errorMessage = "Resource with provided IDs not found";
            LOGGER.debug(errorMessage);
            throw new ResourceNotFoundException(errorMessage);
        }

    }

    @Override
    public FinancialInstitutionTransaction createFinancialInstitutionTransaction(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId, FinancialInstitutionTransaction financialInstitutionTransaction) {
        return getTransactionsRepo(financialInstitutionId, financialInstitutionUserId, financialInstitutionAccountId).create(financialInstitutionTransaction);
    }

    @Override
    public void deleteFinancialInstitutionTransaction(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId, UUID financialInstitutionTransactionId) throws ResourceNotFoundException {
        try {
            getTransactionsRepo(financialInstitutionId, financialInstitutionUserId, financialInstitutionAccountId).delete(financialInstitutionTransactionId);
        } catch (io.crnk.core.exception.ResourceNotFoundException e) {
            String errorMessage = "Resource with provided IDs not found";
            LOGGER.debug(errorMessage);
            throw new ResourceNotFoundException(errorMessage);
        }
    }

    protected ResourceRepositoryV2<FinancialInstitutionTransaction, UUID> getTransactionsRepo(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId){
        String correctPath = SANDBOX_ACCOUNTS_TXN_FI_REQUEST_PATH
                .replace(FINANCIAL_INSTITUTION_ID_TAG, financialInstitutionId.toString())
                .replace(USER_ID_TAG, financialInstitutionUserId.toString())
                .replace(ACCOUNT_ID_TAG, financialInstitutionAccountId.toString())
                ;
        return getApiClient(correctPath, null).getRepositoryForType(FinancialInstitutionTransaction.class);
    }
}
