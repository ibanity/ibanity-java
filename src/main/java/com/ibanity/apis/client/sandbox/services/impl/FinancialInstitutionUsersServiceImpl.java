package com.ibanity.apis.client.sandbox.services.impl;

import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionUser;
import com.ibanity.apis.client.sandbox.services.FinancialInstitutionUsersService;
import com.ibanity.apis.client.services.impl.AbstractServiceImpl;
import com.ibanity.apis.client.services.impl.FinancialInstitutionsServiceImpl;
import io.crnk.core.exception.CrnkMappableException;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import io.crnk.core.resource.list.ResourceList;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

public class FinancialInstitutionUsersServiceImpl extends AbstractServiceImpl implements FinancialInstitutionUsersService {
    private static final Logger LOGGER = LogManager.getLogger(FinancialInstitutionsServiceImpl.class);

    public FinancialInstitutionUsersServiceImpl() {
        super();
    }

    @Override
    public ResourceList<FinancialInstitutionUser> getFinancialInstitutionUsers() {
        return getFinancialInstitutionUsers(new IbanityPagingSpec());
    }

    @Override
    public ResourceList<FinancialInstitutionUser> getFinancialInstitutionUsers(final IbanityPagingSpec pagingSpec) {
        QuerySpec querySpec = new QuerySpec(FinancialInstitutionUser.class);
        querySpec.setPagingSpec(pagingSpec);
        return findAll(querySpec, getFinancialInstitutionUsersRepo(null));
    }

    @Override
    public FinancialInstitutionUser getFinancialInstitutionUser(final UUID financialInstitutionUserId)  throws ApiErrorsException {
        try {
            return getFinancialInstitutionUsersRepo(null).findOne(financialInstitutionUserId, new QuerySpec(FinancialInstitutionUser.class));
        } catch (CrnkMappableException e) {
            LOGGER.debug(e.getErrorData().toString());
            throw new ApiErrorsException(e.getHttpStatus(), e.getErrorData(), e);
        }
    }

    @Override
    public FinancialInstitutionUser createFinancialInstitutionUser(final FinancialInstitutionUser financialInstitutionUser) {
        return getFinancialInstitutionUsersRepo(null).create(financialInstitutionUser);
    }

    @Override
    public FinancialInstitutionUser createFinancialInstitutionUser(final FinancialInstitutionUser financialInstitutionUser, final UUID idempotency) {
        return getFinancialInstitutionUsersRepo(idempotency).create(financialInstitutionUser);
    }

    @Override
    public FinancialInstitutionUser updateFinancialInstitutionUser(final FinancialInstitutionUser financialInstitutionUser) {
        return getFinancialInstitutionUsersRepo(null).save(financialInstitutionUser);
    }

    @Override
    public FinancialInstitutionUser updateFinancialInstitutionUser(final FinancialInstitutionUser financialInstitutionUser, final UUID idempotency) {
        return getFinancialInstitutionUsersRepo(idempotency).save(financialInstitutionUser);
    }

    @Override
    public void deleteFinancialInstitutionUser(final UUID financialInstitutionUserId) throws ApiErrorsException {
        try {
            getFinancialInstitutionUsersRepo(null).delete(financialInstitutionUserId);
        } catch (CrnkMappableException e) {
            LOGGER.debug(e.getErrorData().toString());
            throw new ApiErrorsException(e.getHttpStatus(), e.getErrorData(), e);
        }
    }

    protected ResourceRepositoryV2<FinancialInstitutionUser, UUID> getFinancialInstitutionUsersRepo(final UUID idempotency) {
        String finalPath = StringUtils.removeEnd(
                IbanityConfiguration.getApiIUrls().getSandbox().getFinancialInstitutionUsers()
                        .replace(FinancialInstitutionUser.RESOURCE_PATH, "")
                        .replace(FinancialInstitutionUser.API_URL_TAG_ID, "")
                ,"//");

        return getApiClient(finalPath, null, idempotency).getRepositoryForType(FinancialInstitutionUser.class);
    }
}
