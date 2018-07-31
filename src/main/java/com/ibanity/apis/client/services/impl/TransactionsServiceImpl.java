package com.ibanity.apis.client.services.impl;

import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.models.Account;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.models.Transaction;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.services.TransactionsService;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import io.crnk.core.resource.list.ResourceList;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class TransactionsServiceImpl extends AbstractServiceImpl implements TransactionsService {

    public TransactionsServiceImpl() {
        super();
    }

    @Override
    public ResourceList<Transaction> list(final String customerAccessToken, final UUID financialInstitutionId,
                                          final UUID accountId) {
        return list(customerAccessToken, financialInstitutionId, accountId, new IbanityPagingSpec());
    }

    @Override
    public ResourceList<Transaction> list(final String customerAccessToken, final UUID financialInstitutionId,
                                          final UUID accountId, final IbanityPagingSpec pagingSpec) {
        QuerySpec querySpec = new QuerySpec(Transaction.class).setPagingSpec(pagingSpec);

        return getRepository(customerAccessToken, financialInstitutionId, accountId)
                .findAll(querySpec);
    }

    @Override
    public Transaction find(final String customerAccessToken, final UUID financialInstitutionId,
                            final UUID accountId, final UUID transactionId) {
        return getRepository(customerAccessToken, financialInstitutionId, accountId)
                .findOne(transactionId, new QuerySpec(Transaction.class));
    }

    private ResourceRepositoryV2<Transaction, UUID> getRepository(
            final String customerAccessToken, final UUID financialInstitutionId, final UUID accountId) {
        String finalPath = StringUtils.removeEnd(
                IbanityConfiguration.getApiUrls().getCustomer().getFinancialInstitution().getTransactions()
                        .replace(FinancialInstitution.API_URL_TAG_ID, financialInstitutionId.toString())
                        .replace(Account.API_URL_TAG_ID, accountId.toString())
                        .replace(Transaction.RESOURCE_PATH, "")
                        .replace(Transaction.API_URL_TAG_ID, ""),
                "//");

        return getApiClient(finalPath, customerAccessToken).getRepositoryForType(Transaction.class);
    }
}
