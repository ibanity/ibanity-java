package com.ibanity.apis.client.services.impl;

import com.ibanity.apis.client.exceptions.ResourceNotFoundException;
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
    public ResourceList<Transaction> getAccountTransactions(CustomerAccessToken customerAccessToken, UUID financialInstitutionId, UUID accountId) throws ResourceNotFoundException {
        return getAccountTransactions(customerAccessToken, financialInstitutionId, accountId, new IbanityPagingSpec());
    }

    @Override
    public ResourceList<Transaction> getAccountTransactions(CustomerAccessToken customerAccessToken, UUID financialInstitutionId, UUID accountId, IbanityPagingSpec pagingSpec) throws ResourceNotFoundException {
        QuerySpec querySpec = new QuerySpec(Transaction.class);
        querySpec.setPagingSpec(pagingSpec);
        try {
            return findAll(querySpec, getRepository(customerAccessToken, financialInstitutionId, accountId));
        } catch (io.crnk.core.exception.ResourceNotFoundException e) {
            String errorMessage = "Resource with provided ids not found";
            throw new ResourceNotFoundException(errorMessage);
        }
    }

    @Override
    public Transaction getAccountTransaction(CustomerAccessToken customerAccessToken, UUID financialInstitutionId, UUID accountId, UUID transactionId) throws ResourceNotFoundException {
        try {
            return getRepository(customerAccessToken, financialInstitutionId, accountId).findOne(transactionId, new QuerySpec(Transaction.class));
        } catch (io.crnk.core.exception.ResourceNotFoundException e) {
            String errorMessage = "Resource with provided ids not found";
            throw new ResourceNotFoundException(errorMessage);
        }
    }

    private ResourceRepositoryV2<Transaction, UUID> getRepository(CustomerAccessToken customerAccessToken, UUID financialInstitutionId, UUID accountId){
        String correctPath = TRANSACTIONS_REQUEST_PATH
                .replace(FINANCIAL_INSTITUTION_ID_TAG, financialInstitutionId.toString())
                .replace(ACCOUNT_ID_TAG, accountId.toString());
        return getApiClient(correctPath, customerAccessToken).getRepositoryForType(Transaction.class);
    }
}
