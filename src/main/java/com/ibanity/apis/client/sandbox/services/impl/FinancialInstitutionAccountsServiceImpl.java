package com.ibanity.apis.client.sandbox.services.impl;

import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionAccount;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionUser;
import com.ibanity.apis.client.sandbox.models.factory.create.FinancialInstitutionAccountCreationQuery;
import com.ibanity.apis.client.sandbox.models.factory.delete.FinancialInstitutionAccountDeleteQuery;
import com.ibanity.apis.client.sandbox.models.factory.read.FinancialInstitutionAccountReadQuery;
import com.ibanity.apis.client.sandbox.models.factory.read.FinancialInstitutionAccountsReadQuery;
import com.ibanity.apis.client.sandbox.services.FinancialInstitutionAccountsService;
import com.ibanity.apis.client.services.impl.AbstractServiceImpl;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.UUID;

public class FinancialInstitutionAccountsServiceImpl extends AbstractServiceImpl implements FinancialInstitutionAccountsService {

    @Override
    public FinancialInstitutionAccount find(final FinancialInstitutionAccountReadQuery accountReadQuery) {
        QuerySpec querySpec = new QuerySpec(FinancialInstitutionAccount.class);
        return getRepository(
                accountReadQuery.getFinancialInstitutionId(),
                accountReadQuery.getFinancialInstitutionUserId(),
                null)
                .findOne(accountReadQuery.getFinancialInstitutionAccountId(), querySpec);
    }

    @Override
    public List<FinancialInstitutionAccount> list(final FinancialInstitutionAccountsReadQuery accountsReadQuery) {
        QuerySpec querySpec = new QuerySpec(FinancialInstitutionAccount.class);

        if (accountsReadQuery.getPagingSpec() != null) {
            querySpec.setPagingSpec(accountsReadQuery.getPagingSpec());
        } else {
            querySpec.setPagingSpec(IbanityPagingSpec.DEFAULT_PAGING_SPEC);
        }

        return getRepository(
                accountsReadQuery.getFinancialInstitutionId(),
                accountsReadQuery.getFinancialInstitutionUserId(),
                null)
                .findAll(querySpec);
    }

    @Override
    public FinancialInstitutionAccount create(final FinancialInstitutionAccountCreationQuery query) {
        FinancialInstitutionAccount financialInstitutionAccount = new FinancialInstitutionAccount();

        financialInstitutionAccount.setDescription(query.getDescription());
        financialInstitutionAccount.setReference(query.getReference());
        financialInstitutionAccount.setReferenceType(query.getReferenceType());
        financialInstitutionAccount.setAvailableBalance(query.getAvailableBalance());
        financialInstitutionAccount.setCurrentBalance(query.getCurrentBalance());
        financialInstitutionAccount.setCurrency(query.getCurrency());
        financialInstitutionAccount.setSubType(query.getSubType());

        FinancialInstitution financialInstitution = new FinancialInstitution();
        financialInstitution.setId(query.getFinancialInstitutionId());
        financialInstitutionAccount.setFinancialInstitutionId(financialInstitution.getId());

        return getRepository(
                query.getFinancialInstitutionId(),
                query.getFinancialInstitutionUserId(),
                query.getIdempotencyKey())
                .create(financialInstitutionAccount);
    }

    @Override
    public void delete(final FinancialInstitutionAccountDeleteQuery accountDeleteQuery) {
        getRepository(
                accountDeleteQuery.getFinancialInstitutionId(),
                accountDeleteQuery.getFinancialInstitutionUserId(),
                accountDeleteQuery.getIdempotencyKey())
                .delete(accountDeleteQuery.getFinancialInstitutionAccountId());
    }

    private ResourceRepositoryV2<FinancialInstitutionAccount, UUID> getRepository(final UUID financialInstitutionId, final UUID financialInstitutionUserId, final UUID idempotencyKey) {
        String finalPath = StringUtils.removeEnd(
                IbanityConfiguration.getApiUrls().getSandbox().getFinancialInstitution().getFinancialInstitutionAccounts()
                        .replace(FinancialInstitution.API_URL_TAG_ID, financialInstitutionId.toString())
                        .replace(FinancialInstitutionUser.API_URL_TAG_ID, financialInstitutionUserId.toString())
                        .replace(FinancialInstitutionAccount.RESOURCE_PATH, "")
                        .replace(FinancialInstitutionAccount.API_URL_TAG_ID, ""), "//");
        return getApiClient(finalPath, null, idempotencyKey).getRepositoryForType(FinancialInstitutionAccount.class);
    }
}
