package com.ibanity.apis.client.services.impl;

import com.ibanity.apis.client.exceptions.ResourceNotFoundException;
import com.ibanity.apis.client.models.sandbox.FinancialInstitutionUser;
import com.ibanity.apis.client.paging.PagingSpec;
import com.ibanity.apis.client.services.UsersService;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import io.crnk.core.resource.list.ResourceList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

import static com.ibanity.apis.client.services.configuration.IBanityConfiguration.SANBOX_PREFIX_PATH;

public class UsersServiceImpl extends AbstractServiceImpl implements UsersService {
    private static final Logger LOGGER = LogManager.getLogger(FinancialInstitutionsServiceImpl.class);

    private final ResourceRepositoryV2<FinancialInstitutionUser, UUID> sandboxfFinancialInstitutionUserRepo;

    public UsersServiceImpl() {
        super();
        sandboxfFinancialInstitutionUserRepo = getApiClient(SANBOX_PREFIX_PATH+"/").getRepositoryForType(FinancialInstitutionUser.class);
    }

    @Override
    public ResourceList<FinancialInstitutionUser> getSandboxFinancialInstitutionUsers() {
        return getSandboxFinancialInstitutionUsers(new PagingSpec());
    }

    @Override
    public ResourceList<FinancialInstitutionUser> getSandboxFinancialInstitutionUsers(PagingSpec pagingSpec) {
        QuerySpec querySpec = new QuerySpec(FinancialInstitutionUser.class);
        querySpec.setPagingSpec(pagingSpec);
        return findAll(querySpec, sandboxfFinancialInstitutionUserRepo);
    }

    @Override
    public FinancialInstitutionUser getSandboxFinancialInstitutionUser(UUID sandboxFinancialInstitutionUserId)  throws ResourceNotFoundException {
        try {
            return sandboxfFinancialInstitutionUserRepo.findOne(sandboxFinancialInstitutionUserId, new QuerySpec(FinancialInstitutionUser.class));
        } catch (io.crnk.core.exception.ResourceNotFoundException e) {
            String errorMessage = "Resource with ID:"+sandboxFinancialInstitutionUserId+": not found";
            LOGGER.debug(errorMessage);
            throw new ResourceNotFoundException(errorMessage);
        }
    }

    @Override
    public FinancialInstitutionUser createSandboxFinancialInstitutionUser(FinancialInstitutionUser sandboxFinancialInstitutionUser) {
        return sandboxfFinancialInstitutionUserRepo.create(sandboxFinancialInstitutionUser);
    }

    @Override
    public FinancialInstitutionUser updateSandboxFinancialInstitutionUser(FinancialInstitutionUser sandboxFinancialInstitutionUser) {
        return sandboxfFinancialInstitutionUserRepo.save(sandboxFinancialInstitutionUser);
    }

    @Override
    public void deleteSandboxFinancialInstitutionUser(UUID sandboxFinancialInstitutionUserId) throws ResourceNotFoundException {
        try {
            sandboxfFinancialInstitutionUserRepo.delete(sandboxFinancialInstitutionUserId);
        } catch (io.crnk.core.exception.ResourceNotFoundException e) {
            String errorMessage = "Resource with ID:"+sandboxFinancialInstitutionUserId+": not found";
            LOGGER.debug(errorMessage);
            throw new ResourceNotFoundException(errorMessage);
        }
    }
}
