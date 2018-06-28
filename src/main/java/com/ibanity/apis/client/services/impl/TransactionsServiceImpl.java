package com.ibanity.apis.client.services.impl;

import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.models.CustomerAccessToken;
import com.ibanity.apis.client.models.Transaction;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.services.TransactionsService;
import io.crnk.core.exception.CrnkMappableException;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import io.crnk.core.resource.list.ResourceList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

import static com.ibanity.apis.client.configuration.IbanityConfiguration.FORWARD_SLASH;

public class TransactionsServiceImpl extends AbstractServiceImpl implements TransactionsService {

    private static final Logger LOGGER = LogManager.getLogger(TransactionsServiceImpl.class);

    private static final String TRANSACTIONS_REQUEST_PATH = FORWARD_SLASH + "customer" + FORWARD_SLASH + "financial-institutions" + FORWARD_SLASH + FINANCIAL_INSTITUTION_ID_TAG + FORWARD_SLASH + "accounts" + FORWARD_SLASH + ACCOUNT_ID_TAG;

    public TransactionsServiceImpl() {
        super();
    }

    @Override
    public ResourceList<Transaction> getAccountTransactions(final CustomerAccessToken customerAccessToken, final UUID financialInstitutionId, final UUID accountId) throws ApiErrorsException {
        return getAccountTransactions(customerAccessToken, financialInstitutionId, accountId, new IbanityPagingSpec());
    }

    @Override
    public ResourceList<Transaction> getAccountTransactions(final CustomerAccessToken customerAccessToken, final UUID financialInstitutionId, final UUID accountId, final IbanityPagingSpec pagingSpec) throws ApiErrorsException {
        QuerySpec querySpec = new QuerySpec(Transaction.class);
        querySpec.setPagingSpec(pagingSpec);
        try {
            return findAll(querySpec, getRepository(customerAccessToken, financialInstitutionId, accountId));
        } catch (CrnkMappableException e) {
            LOGGER.debug(e.getErrorData().toString());
            throw new ApiErrorsException(e.getHttpStatus(), e.getErrorData(), e);
        }
    }

    @Override
    public Transaction getAccountTransaction(final CustomerAccessToken customerAccessToken, final UUID financialInstitutionId, final UUID accountId, final UUID transactionId) throws ApiErrorsException {
        try {
            return getRepository(customerAccessToken, financialInstitutionId, accountId).findOne(transactionId, new QuerySpec(Transaction.class));
        } catch (CrnkMappableException e) {
            LOGGER.debug(e.getErrorData().toString());
            throw new ApiErrorsException(e.getHttpStatus(), e.getErrorData(), e);
        }
    }

    private ResourceRepositoryV2<Transaction, UUID> getRepository(final CustomerAccessToken customerAccessToken, final UUID financialInstitutionId, final UUID accountId) {
        String correctPath = TRANSACTIONS_REQUEST_PATH
                .replace(FINANCIAL_INSTITUTION_ID_TAG, financialInstitutionId.toString())
                .replace(ACCOUNT_ID_TAG, accountId.toString());
        return getApiClient(correctPath, customerAccessToken).getRepositoryForType(Transaction.class);
    }
}
