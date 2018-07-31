package com.ibanity.apis.client.sandbox.services.impl;

import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionAccount;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionTransaction;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionUser;
import com.ibanity.apis.client.sandbox.services.FinancialInstitutionTransactionsService;
import com.ibanity.apis.client.services.impl.AbstractServiceImpl;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.UUID;

public class FinancialInstitutionTransactionsServiceImpl extends AbstractServiceImpl implements FinancialInstitutionTransactionsService {

    @Override
    public FinancialInstitutionTransaction find(final UUID financialInstitutionId, final UUID financialInstitutionUserId, final UUID financialInstitutionAccountId, final UUID financialInstitutionTransactionId) throws ApiErrorsException {
        ResourceRepositoryV2<FinancialInstitutionTransaction, UUID> transactionsRepo = getRepository(financialInstitutionId, financialInstitutionUserId, financialInstitutionAccountId, null);
        QuerySpec querySpec = new QuerySpec(FinancialInstitutionTransaction.class);
        return transactionsRepo.findOne(financialInstitutionTransactionId, querySpec);
    }

    @Override
    public List<FinancialInstitutionTransaction> list(final UUID financialInstitutionId, final UUID financialInstitutionUserId, final UUID financialInstitutionAccountId) throws ApiErrorsException {
        ResourceRepositoryV2<FinancialInstitutionTransaction, UUID> transactionsRepo = getRepository(financialInstitutionId, financialInstitutionUserId, financialInstitutionAccountId, null);
        QuerySpec querySpec = new QuerySpec(FinancialInstitutionTransaction.class);
        return transactionsRepo.findAll(querySpec);
    }

    @Override
    public FinancialInstitutionTransaction create(final UUID financialInstitutionId, final UUID financialInstitutionUserId, final UUID financialInstitutionAccountId, final FinancialInstitutionTransaction financialInstitutionTransaction) throws ApiErrorsException {
        return internalCreate(financialInstitutionId, financialInstitutionUserId, financialInstitutionAccountId, financialInstitutionTransaction, null);
    }

    @Override
    public FinancialInstitutionTransaction create(final UUID financialInstitutionId, final UUID financialInstitutionUserId, final UUID financialInstitutionAccountId, final FinancialInstitutionTransaction financialInstitutionTransaction, final UUID idempotencyKey) throws ApiErrorsException {
        return internalCreate(financialInstitutionId, financialInstitutionUserId, financialInstitutionAccountId, financialInstitutionTransaction, idempotencyKey);
    }

    @Override
    public void delete(final UUID financialInstitutionId, final UUID financialInstitutionUserId, final UUID financialInstitutionAccountId, final UUID financialInstitutionTransactionId) throws ApiErrorsException {
        getRepository(financialInstitutionId, financialInstitutionUserId, financialInstitutionAccountId, null).delete(financialInstitutionTransactionId);
    }

    private FinancialInstitutionTransaction internalCreate(final UUID financialInstitutionId, final UUID financialInstitutionUserId, final UUID financialInstitutionAccountId, final FinancialInstitutionTransaction financialInstitutionTransaction, final UUID idempotencyKey) throws ApiErrorsException {
        return getRepository(financialInstitutionId, financialInstitutionUserId, financialInstitutionAccountId, idempotencyKey).create(financialInstitutionTransaction);
    }

    private ResourceRepositoryV2<FinancialInstitutionTransaction, UUID> getRepository(final UUID financialInstitutionId, final UUID financialInstitutionUserId, final UUID financialInstitutionAccountId, final UUID idempotencyKey) {
        String finalPath = StringUtils.removeEnd(
                IbanityConfiguration.getApiUrls().getSandbox().getFinancialInstitution().getFinancialInstitutionAccount().getFinancialInstitutionTransactions()
                        .replace(FinancialInstitution.API_URL_TAG_ID, financialInstitutionId.toString())
                        .replace(FinancialInstitutionUser.API_URL_TAG_ID, financialInstitutionUserId.toString())
                        .replace(FinancialInstitutionAccount.API_URL_TAG_ID, financialInstitutionAccountId.toString())
                        .replace(FinancialInstitutionTransaction.RESOURCE_PATH, "")
                        .replace(FinancialInstitutionTransaction.API_URL_TAG_ID, ""),
                "//");

        return getApiClient(finalPath, null, idempotencyKey).getRepositoryForType(FinancialInstitutionTransaction.class);
    }
}
