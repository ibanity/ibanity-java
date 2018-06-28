package com.ibanity.apis.client.sandbox.services.impl;

import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionAccount;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionUser;
import com.ibanity.apis.client.sandbox.services.FinancialInstitutionAccountsService;
import com.ibanity.apis.client.services.impl.AbstractServiceImpl;
import io.crnk.core.exception.CrnkMappableException;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.UUID;

public class FinancialInstitutionAccountsServiceImpl extends AbstractServiceImpl implements FinancialInstitutionAccountsService {

    private static final Logger LOGGER = LogManager.getLogger(FinancialInstitutionAccountsServiceImpl.class);

    @Override
    public FinancialInstitutionAccount getFinancialInstitutionAccount(final UUID financialInstitutionId, final UUID financialInstitutionUserId, final UUID financialInstitutionAccountId) throws ApiErrorsException {
        ResourceRepositoryV2<FinancialInstitutionAccount, UUID> accountsRepo = getAccountsRepository(financialInstitutionId, financialInstitutionUserId, null);
        QuerySpec querySpec = new QuerySpec(FinancialInstitutionAccount.class);
        try {
            return accountsRepo.findOne(financialInstitutionAccountId, querySpec);
        } catch (CrnkMappableException e) {
            LOGGER.debug(e.getErrorData().toString());
            throw new ApiErrorsException(e.getHttpStatus(), e.getErrorData(), e);
        }
    }

    @Override
    public List<FinancialInstitutionAccount> getFinancialInstitutionUserAccounts(final UUID financialInstitutionId, final UUID financialInstitutionUserId) throws ApiErrorsException {
        try {
            return getAccountsRepository(financialInstitutionId, financialInstitutionUserId, null).findAll(new QuerySpec(FinancialInstitutionAccount.class));
        } catch (CrnkMappableException e) {
            LOGGER.debug(e.getErrorData().toString());
            throw new ApiErrorsException(e.getHttpStatus(), e.getErrorData(), e);
        }
    }

    @Override
    public FinancialInstitutionAccount createFinancialInstitutionAccount(final UUID financialInstitutionId, final UUID financialInstitutionUserId, final FinancialInstitutionAccount financialInstitutionAccount) throws ApiErrorsException {
        return create(financialInstitutionId, financialInstitutionUserId, financialInstitutionAccount, null);
    }

    private FinancialInstitutionAccount create(final UUID financialInstitutionId, final UUID financialInstitutionUserId, final FinancialInstitutionAccount financialInstitutionAccount, final UUID idempotency) throws ApiErrorsException {
        try {
            return getAccountsRepository(financialInstitutionId, financialInstitutionUserId, idempotency).create(financialInstitutionAccount);
        } catch (CrnkMappableException e) {
            LOGGER.debug(e.getErrorData().toString());
            throw new ApiErrorsException(e.getHttpStatus(), e.getErrorData(), e);
        }
    }

    @Override
    public FinancialInstitutionAccount createFinancialInstitutionAccount(final UUID financialInstitutionId, final UUID financialInstitutionUserId, final FinancialInstitutionAccount financialInstitutionAccount, final UUID idempotency) throws ApiErrorsException {
        return create(financialInstitutionId, financialInstitutionUserId, financialInstitutionAccount, idempotency);
    }

    @Override
    public void deleteFinancialInstitutionAccount(final UUID financialInstitutionId, final UUID financialInstitutionUserId, final UUID financialInstitutionAccountId) throws ApiErrorsException {
        try {
            getAccountsRepository(financialInstitutionId, financialInstitutionUserId, null).delete(financialInstitutionAccountId);
        } catch (CrnkMappableException e) {
            LOGGER.debug(e.getErrorData().toString());
            throw new ApiErrorsException(e.getHttpStatus(), e.getErrorData(), e);
        }
    }

    protected ResourceRepositoryV2<FinancialInstitutionAccount, UUID> getAccountsRepository(final UUID financialInstitutionId, final UUID financialInstitutionUserId, final UUID idempotency) {
        String finalPath = StringUtils.removeEnd(
                IbanityConfiguration.getApiIUrls().getSandbox().getFinancialInstitution().getFinancialInstitutionAccounts()
                        .replace(FinancialInstitution.API_URL_TAG_ID, financialInstitutionId.toString())
                        .replace(FinancialInstitutionUser.API_URL_TAG_ID, financialInstitutionUserId.toString())
                        .replace(FinancialInstitutionAccount.RESOURCE_PATH, "")
                        .replace(FinancialInstitutionAccount.API_URL_TAG_ID, "")
                ,"//");
        return getApiClient(finalPath, null, idempotency).getRepositoryForType(FinancialInstitutionAccount.class);
    }
}
