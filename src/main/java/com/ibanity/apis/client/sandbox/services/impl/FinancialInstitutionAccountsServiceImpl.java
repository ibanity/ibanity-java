package com.ibanity.apis.client.sandbox.services.impl;

import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionAccount;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionUser;
import com.ibanity.apis.client.sandbox.services.FinancialInstitutionAccountsService;
import com.ibanity.apis.client.services.impl.AbstractServiceImpl;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.UUID;

public class FinancialInstitutionAccountsServiceImpl extends AbstractServiceImpl implements FinancialInstitutionAccountsService {

    @Override
    public FinancialInstitutionAccount find(final UUID financialInstitutionId, final UUID financialInstitutionUserId, final UUID financialInstitutionAccountId) throws ApiErrorsException {
        ResourceRepositoryV2<FinancialInstitutionAccount, UUID> accountsRepo = getRepository(financialInstitutionId, financialInstitutionUserId, null);
        QuerySpec querySpec = new QuerySpec(FinancialInstitutionAccount.class);
        return accountsRepo.findOne(financialInstitutionAccountId, querySpec);
    }

    @Override
    public List<FinancialInstitutionAccount> list(final UUID financialInstitutionId, final UUID financialInstitutionUserId) throws ApiErrorsException {
        return getRepository(financialInstitutionId, financialInstitutionUserId, null).findAll(new QuerySpec(FinancialInstitutionAccount.class));
    }

    @Override
    public FinancialInstitutionAccount create(final UUID financialInstitutionId, final UUID financialInstitutionUserId, final FinancialInstitutionAccount financialInstitutionAccount) throws ApiErrorsException {
        return internalCreate(financialInstitutionId, financialInstitutionUserId, financialInstitutionAccount, null);
    }

    @Override
    public FinancialInstitutionAccount create(final UUID financialInstitutionId, final UUID financialInstitutionUserId, final FinancialInstitutionAccount financialInstitutionAccount, final UUID idempotencyKey) throws ApiErrorsException {
        return internalCreate(financialInstitutionId, financialInstitutionUserId, financialInstitutionAccount, idempotencyKey);
    }

    @Override
    public void delete(final UUID financialInstitutionId, final UUID financialInstitutionUserId, final UUID financialInstitutionAccountId) throws ApiErrorsException {
        getRepository(financialInstitutionId, financialInstitutionUserId, null).delete(financialInstitutionAccountId);
    }

    private FinancialInstitutionAccount internalCreate(final UUID financialInstitutionId, final UUID financialInstitutionUserId, final FinancialInstitutionAccount financialInstitutionAccount, final UUID idempotencyKey) throws ApiErrorsException {
        return getRepository(financialInstitutionId, financialInstitutionUserId, idempotencyKey).create(financialInstitutionAccount);
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
