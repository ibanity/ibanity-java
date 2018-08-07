package com.ibanity.apis.client.sandbox.services.impl;

import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionAccount;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionTransaction;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionUser;
import com.ibanity.apis.client.sandbox.models.factory.create.FinancialInstitutionTransactionCreationQuery;
import com.ibanity.apis.client.sandbox.models.factory.delete.FinancialInstitutionTransactionDeleteQuery;
import com.ibanity.apis.client.sandbox.models.factory.read.FinancialInstitutionTransactionReadQuery;
import com.ibanity.apis.client.sandbox.models.factory.read.FinancialInstitutionTransactionsReadQuery;
import com.ibanity.apis.client.sandbox.services.FinancialInstitutionTransactionsService;
import com.ibanity.apis.client.services.impl.AbstractServiceImpl;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.UUID;

public class FinancialInstitutionTransactionsServiceImpl extends AbstractServiceImpl implements FinancialInstitutionTransactionsService {

    @Override
    public FinancialInstitutionTransaction find(final FinancialInstitutionTransactionReadQuery transactionReadQuery) {
        ResourceRepositoryV2<FinancialInstitutionTransaction, UUID> transactionsRepo =
                getRepository(transactionReadQuery.getFinancialInstitutionId(),
                        transactionReadQuery.getFinancialInstitutionUserId(),
                        transactionReadQuery.getFinancialInstitutionAccountId(),
                        transactionReadQuery.getIdempotencyKey());
        QuerySpec querySpec = new QuerySpec(FinancialInstitutionTransaction.class);
        return transactionsRepo.findOne(transactionReadQuery.getFinancialInstitutionTransactionId(), querySpec);
    }

    @Override
    public List<FinancialInstitutionTransaction> list(final FinancialInstitutionTransactionsReadQuery transactionsReadQuery) {
        ResourceRepositoryV2<FinancialInstitutionTransaction, UUID> transactionsRepo =
                getRepository(transactionsReadQuery.getFinancialInstitutionId(),
                        transactionsReadQuery.getFinancialInstitutionUserId(),
                        transactionsReadQuery.getFinancialInstitutionAccountId(),
                        transactionsReadQuery.getIdempotencyKey());
        QuerySpec querySpec = new QuerySpec(FinancialInstitutionTransaction.class);

        if (transactionsReadQuery.getPagingSpec() != null) {
            querySpec.setPagingSpec(transactionsReadQuery.getPagingSpec());
        } else {
            querySpec.setPagingSpec(IbanityPagingSpec.DEFAULT_PAGING_SPEC);
        }
        return transactionsRepo.findAll(querySpec);
    }

    @Override
    public FinancialInstitutionTransaction create(final FinancialInstitutionTransactionCreationQuery transactionCreationQuery) {
        FinancialInstitutionTransaction financialInstitutionTransaction = new FinancialInstitutionTransaction();

        financialInstitutionTransaction.setAmount(transactionCreationQuery.getAmount());
        financialInstitutionTransaction.setCurrency(transactionCreationQuery.getCurrency());
        financialInstitutionTransaction.setRemittanceInformation(transactionCreationQuery.getRemittanceInformation());
        financialInstitutionTransaction.setRemittanceInformationType(transactionCreationQuery.getRemittanceInformationType());
        financialInstitutionTransaction.setCounterpartName(transactionCreationQuery.getCounterpartName());
        financialInstitutionTransaction.setCounterpartReference(transactionCreationQuery.getCounterpartReference());
        financialInstitutionTransaction.setValueDate(transactionCreationQuery.getValueDate());
        financialInstitutionTransaction.setExecutionDate(transactionCreationQuery.getExecutionDate());
        financialInstitutionTransaction.setDescription(transactionCreationQuery.getDescription());

        FinancialInstitutionAccount financialInstitutionAccount =
                new FinancialInstitutionAccount();
        financialInstitutionAccount.setId(transactionCreationQuery.getFinancialInstitutionAccountId());
        financialInstitutionTransaction.setFinancialInstitutionAccount(financialInstitutionAccount);

        return getRepository(
                transactionCreationQuery.getFinancialInstitutionId(),
                transactionCreationQuery.getFinancialInstitutionUserId(),
                transactionCreationQuery.getFinancialInstitutionAccountId(),
                transactionCreationQuery.getIdempotencyKey()
        ).create(financialInstitutionTransaction);
    }

    @Override
    public void delete(final FinancialInstitutionTransactionDeleteQuery transactionDeleteQuery) {
        getRepository(transactionDeleteQuery.getFinancialInstitutionId(),
                transactionDeleteQuery.getFinancialInstitutionUserId(),
                transactionDeleteQuery.getFinancialInstitutionAccountId(),
                transactionDeleteQuery.getIdempotencyKey())
                .delete(transactionDeleteQuery.getFinancialInstitutionTransactionId());
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
