package com.ibanity.apis.client.sandbox.services.impl;

import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionTransaction;
import com.ibanity.apis.client.sandbox.services.FinancialInstitutionTransactionsService;
import com.ibanity.apis.client.services.impl.AbstractServiceImpl;
import io.crnk.core.exception.CrnkMappableException;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.UUID;

import static com.ibanity.apis.client.configuration.IbanityConfiguration.FORWARD_SLASH;
import static com.ibanity.apis.client.configuration.IbanityConfiguration.SANBOX_PREFIX_PATH;

public class FinancialInstitutionTransactionsServiceImpl extends AbstractServiceImpl implements FinancialInstitutionTransactionsService {
    private static final String SANDBOX_ACCOUNTS_FI_REQUEST_PATH            = SANBOX_PREFIX_PATH + FORWARD_SLASH + FINANCIAL_INSTITUTIONS_PATH + FORWARD_SLASH + FINANCIAL_INSTITUTION_ID_TAG;
    private static final String SANDBOX_USER_ACCOUNTS_FI_REQUEST_PATH       = SANDBOX_ACCOUNTS_FI_REQUEST_PATH + FORWARD_SLASH + "financial-institution-users" + FORWARD_SLASH + USER_ID_TAG;
    private static final String SANDBOX_ACCOUNTS_TXN_FI_REQUEST_PATH        = SANDBOX_USER_ACCOUNTS_FI_REQUEST_PATH + FORWARD_SLASH + "financial-institution-accounts" + FORWARD_SLASH + ACCOUNT_ID_TAG;

    private static final String ERROR_RESOURCE_NOT_FOUND                    = "Resource with provided IDs not found";

    private static final Logger LOGGER = LogManager.getLogger(FinancialInstitutionTransactionsServiceImpl.class);

    @Override
    public FinancialInstitutionTransaction getFinancialInstitutionTransaction(final UUID financialInstitutionId, final UUID financialInstitutionUserId, final UUID financialInstitutionAccountId, final UUID financialInstitutionTransactionId) throws ApiErrorsException {
        ResourceRepositoryV2<FinancialInstitutionTransaction, UUID> transactionsRepo = getTransactionsRepo(financialInstitutionId, financialInstitutionUserId, financialInstitutionAccountId, null);
        QuerySpec querySpec = new QuerySpec(FinancialInstitutionTransaction.class);
        try {
            return transactionsRepo.findOne(financialInstitutionTransactionId, querySpec);
        } catch (ApiErrorsException e) {
            LOGGER.debug(e.toString());
            throw e;
        } catch (CrnkMappableException e) {
            LOGGER.debug(e.getErrorData().toString());
            throw new ApiErrorsException(e.getHttpStatus(), e.getErrorData(), e);
        }
    }

    @Override
    public List<FinancialInstitutionTransaction> getFinancialInstitutionAccountTransactions(final UUID financialInstitutionId, final UUID financialInstitutionUserId, final UUID financialInstitutionAccountId) throws ApiErrorsException {
        ResourceRepositoryV2<FinancialInstitutionTransaction, UUID> transactionsRepo = getTransactionsRepo(financialInstitutionId, financialInstitutionUserId, financialInstitutionAccountId, null);
        QuerySpec querySpec = new QuerySpec(FinancialInstitutionTransaction.class);
        try {
            return transactionsRepo.findAll(querySpec);
        } catch (ApiErrorsException e) {
            LOGGER.debug(e.toString());
            throw e;
        } catch (CrnkMappableException e) {
            LOGGER.debug(e.getErrorData().toString());
            throw new ApiErrorsException(e.getHttpStatus(), e.getErrorData(), e);
        }
    }

    @Override
    public FinancialInstitutionTransaction createFinancialInstitutionTransaction(final UUID financialInstitutionId, final UUID financialInstitutionUserId, final UUID financialInstitutionAccountId, final FinancialInstitutionTransaction financialInstitutionTransaction) throws ApiErrorsException {
        return create(financialInstitutionId, financialInstitutionUserId, financialInstitutionAccountId, financialInstitutionTransaction, null);
    }


    private FinancialInstitutionTransaction create(final UUID financialInstitutionId, final UUID financialInstitutionUserId, final UUID financialInstitutionAccountId, final FinancialInstitutionTransaction financialInstitutionTransaction, final UUID idempotency) throws ApiErrorsException {
        try {
            return getTransactionsRepo(financialInstitutionId, financialInstitutionUserId, financialInstitutionAccountId, idempotency).create(financialInstitutionTransaction);
        } catch (ApiErrorsException e) {
            LOGGER.debug(e.toString());
            throw e;
        } catch (CrnkMappableException e) {
            LOGGER.debug(e.getErrorData().toString());
            throw new ApiErrorsException(e.getHttpStatus(), e.getErrorData(), e);
        }
    }

    @Override
    public FinancialInstitutionTransaction createFinancialInstitutionTransaction(final UUID financialInstitutionId, final UUID financialInstitutionUserId, final UUID financialInstitutionAccountId, final FinancialInstitutionTransaction financialInstitutionTransaction, final UUID idempotency) throws ApiErrorsException {
        return create(financialInstitutionId, financialInstitutionUserId, financialInstitutionAccountId, financialInstitutionTransaction, idempotency);
    }

    @Override
    public void deleteFinancialInstitutionTransaction(final UUID financialInstitutionId, final UUID financialInstitutionUserId, final UUID financialInstitutionAccountId, final UUID financialInstitutionTransactionId) throws ApiErrorsException {
        try {
            getTransactionsRepo(financialInstitutionId, financialInstitutionUserId, financialInstitutionAccountId, null).delete(financialInstitutionTransactionId);
        } catch (ApiErrorsException e) {
            LOGGER.debug(e.toString());
            throw e;
        } catch (CrnkMappableException e) {
            LOGGER.debug(e.getErrorData().toString());
            throw new ApiErrorsException(e.getHttpStatus(), e.getErrorData(), e);
        }
    }

    protected ResourceRepositoryV2<FinancialInstitutionTransaction, UUID> getTransactionsRepo(final UUID financialInstitutionId, final UUID financialInstitutionUserId, final UUID financialInstitutionAccountId, final UUID idempotency) {
        String correctPath = SANDBOX_ACCOUNTS_TXN_FI_REQUEST_PATH
                .replace(FINANCIAL_INSTITUTION_ID_TAG, financialInstitutionId.toString())
                .replace(USER_ID_TAG, financialInstitutionUserId.toString())
                .replace(ACCOUNT_ID_TAG, financialInstitutionAccountId.toString());
        return getApiClient(correctPath, null, idempotency).getRepositoryForType(FinancialInstitutionTransaction.class);
    }
}
