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

    private final ResourceRepositoryV2<FinancialInstitutionUser, UUID> financialInstitutionUserRepo;

    public FinancialInstitutionUsersServiceImpl() {
        super();
        financialInstitutionUserRepo = getApiClient(SANBOX_PREFIX_PATH + FORWARD_SLASH).getRepositoryForType(FinancialInstitutionUser.class);
    }

    @Override
    public ResourceList<FinancialInstitutionUser> getFinancialInstitutionUsers() {
        return getFinancialInstitutionUsers(new IbanityPagingSpec());
    }

    @Override
    public ResourceList<FinancialInstitutionUser> getFinancialInstitutionUsers(IbanityPagingSpec pagingSpec) {
        QuerySpec querySpec = new QuerySpec(FinancialInstitutionUser.class);
        querySpec.setPagingSpec(pagingSpec);
        return findAll(querySpec, financialInstitutionUserRepo);
    }

    @Override
    public FinancialInstitutionUser getFinancialInstitutionUser(UUID financialInstitutionUserId)  throws ResourceNotFoundException {
        try {
            return financialInstitutionUserRepo.findOne(financialInstitutionUserId, new QuerySpec(FinancialInstitutionUser.class));
        } catch (io.crnk.core.exception.ResourceNotFoundException e) {
            String errorMessage = "Resource with ID:"+financialInstitutionUserId+": not found";
            LOGGER.debug(errorMessage);
            throw new ResourceNotFoundException(errorMessage);
        }
    }

    @Override
    public FinancialInstitutionUser createFinancialInstitutionUser(FinancialInstitutionUser financialInstitutionUser) {
        return financialInstitutionUserRepo.create(financialInstitutionUser);
    }

    @Override
    public FinancialInstitutionUser updateFinancialInstitutionUser(FinancialInstitutionUser financialInstitutionUser) {
        return financialInstitutionUserRepo.save(financialInstitutionUser);
    }

    @Override
    public void deleteFinancialInstitutionUser(UUID financialInstitutionUserId) throws ResourceNotFoundException {
        try {
            financialInstitutionUserRepo.delete(financialInstitutionUserId);
        } catch (io.crnk.core.exception.ResourceNotFoundException e) {
            String errorMessage = "Resource with ID:"+financialInstitutionUserId+": not found";
            LOGGER.debug(errorMessage);
            throw new ResourceNotFoundException(errorMessage);
        }
    }
}
