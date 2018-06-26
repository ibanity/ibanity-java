package com.ibanity.apis.client.sandbox.services.impl;

import com.ibanity.apis.client.exceptions.ResourceNotFoundException;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionUser;
import com.ibanity.apis.client.sandbox.services.FinancialInstitutionUsersService;
import com.ibanity.apis.client.services.impl.AbstractServiceImpl;
import com.ibanity.apis.client.services.impl.FinancialInstitutionsServiceImpl;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import io.crnk.core.resource.list.ResourceList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

import static com.ibanity.apis.client.services.configuration.IbanityConfiguration.FORWARD_SLASH;
import static com.ibanity.apis.client.services.configuration.IbanityConfiguration.SANBOX_PREFIX_PATH;

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
    public ResourceList<FinancialInstitutionUser> getFinancialInstitutionUsers(IbanityPagingSpec pagingSpec) {
        QuerySpec querySpec = new QuerySpec(FinancialInstitutionUser.class);
        querySpec.setPagingSpec(pagingSpec);
        return findAll(querySpec, getFinancialInstitutionUsersRepo(null));
    }

    @Override
    public FinancialInstitutionUser getFinancialInstitutionUser(UUID financialInstitutionUserId)  throws ResourceNotFoundException {
        try {
            return getFinancialInstitutionUsersRepo(null).findOne(financialInstitutionUserId, new QuerySpec(FinancialInstitutionUser.class));
        } catch (io.crnk.core.exception.ResourceNotFoundException e) {
            String errorMessage = "Resource with ID:"+financialInstitutionUserId+": not found";
            LOGGER.debug(errorMessage);
            throw new ResourceNotFoundException(errorMessage);
        }
    }

    @Override
    public FinancialInstitutionUser createFinancialInstitutionUser(FinancialInstitutionUser financialInstitutionUser) {
        return getFinancialInstitutionUsersRepo(null).create(financialInstitutionUser);
    }

    @Override
    public FinancialInstitutionUser createFinancialInstitutionUser(FinancialInstitutionUser financialInstitutionUser, UUID idempotency) {
        return getFinancialInstitutionUsersRepo(idempotency).create(financialInstitutionUser);
    }

    @Override
    public FinancialInstitutionUser updateFinancialInstitutionUser(FinancialInstitutionUser financialInstitutionUser) {
        return getFinancialInstitutionUsersRepo(null).save(financialInstitutionUser);
    }

    @Override
    public FinancialInstitutionUser updateFinancialInstitutionUser(FinancialInstitutionUser financialInstitutionUser, UUID idempotency) {
        return getFinancialInstitutionUsersRepo(idempotency).save(financialInstitutionUser);
    }

    @Override
    public void deleteFinancialInstitutionUser(UUID financialInstitutionUserId) throws ResourceNotFoundException {
        try {
            getFinancialInstitutionUsersRepo(null).delete(financialInstitutionUserId);
        } catch (io.crnk.core.exception.ResourceNotFoundException e) {
            String errorMessage = "Resource with ID:"+financialInstitutionUserId+": not found";
            LOGGER.debug(errorMessage);
            throw new ResourceNotFoundException(errorMessage);
        }
    }

    protected ResourceRepositoryV2<FinancialInstitutionUser, UUID> getFinancialInstitutionUsersRepo(UUID idempotency){
        return getApiClient(SANBOX_PREFIX_PATH + FORWARD_SLASH, null, idempotency).getRepositoryForType(FinancialInstitutionUser.class);
    }
}
