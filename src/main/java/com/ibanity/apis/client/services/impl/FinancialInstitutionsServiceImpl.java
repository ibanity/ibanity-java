package com.ibanity.apis.client.services.impl;

import com.ibanity.apis.client.exceptions.ResourceNotFoundException;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.paging.PagingSpec;
import com.ibanity.apis.client.services.FinancialInstitutionsService;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import io.crnk.core.resource.list.ResourceList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

import static com.ibanity.apis.client.services.configuration.IBanityConfiguration.SANBOX_PREFIX_PATH;

public class FinancialInstitutionsServiceImpl extends AbstractServiceImpl implements FinancialInstitutionsService{
    private static final Logger LOGGER = LogManager.getLogger(FinancialInstitutionsServiceImpl.class);

    private final ResourceRepositoryV2<FinancialInstitution, UUID> financialInstitutionsRepo;
    private final ResourceRepositoryV2<FinancialInstitution, UUID> sandboxFinancialInstitutionsRepo;

    public FinancialInstitutionsServiceImpl() {
        super();
        financialInstitutionsRepo = getApiClient("/").getRepositoryForType(FinancialInstitution.class);
        sandboxFinancialInstitutionsRepo = getApiClient(SANBOX_PREFIX_PATH+"/").getRepositoryForType(FinancialInstitution.class);
    }

    @Override
    public ResourceList<FinancialInstitution> getFinancialInstitutions() {
        return getFinancialInstitutions(new PagingSpec());
    }

    @Override
    public ResourceList<FinancialInstitution> getFinancialInstitutions(PagingSpec pagingSpec) {
        QuerySpec querySpec = new QuerySpec(FinancialInstitution.class);
        querySpec.setPagingSpec(pagingSpec);
        return findAll(querySpec, financialInstitutionsRepo);
    }

    @Override
    public FinancialInstitution getFinancialInstitution(UUID financialInstitutionId) throws ResourceNotFoundException {
        try {
            return financialInstitutionsRepo.findOne(financialInstitutionId, new QuerySpec(FinancialInstitution.class));
        } catch (io.crnk.core.exception.ResourceNotFoundException e) {
            String errorMessage = "Resource with ID:"+financialInstitutionId+": not found";
            LOGGER.debug(errorMessage);
            throw new ResourceNotFoundException(errorMessage);
        }
    }

    @Override
    public FinancialInstitution createSandboxFinancialInstitution(FinancialInstitution financialInstitution) {
        financialInstitution.setSandbox(Boolean.TRUE);
        return sandboxFinancialInstitutionsRepo.create(financialInstitution);
    }

    @Override
    public FinancialInstitution updateSandboxFinancialInstitution(FinancialInstitution financialInstitution) {
        return sandboxFinancialInstitutionsRepo.save(financialInstitution);
    }

    @Override
    public void deleteSandboxFinancialInstitution(UUID financialInstitutionId) throws ResourceNotFoundException {
        try {
            sandboxFinancialInstitutionsRepo.delete(financialInstitutionId);
        } catch (io.crnk.core.exception.ResourceNotFoundException e) {
            String errorMessage = "Resource with ID:"+financialInstitutionId+": not found";
            LOGGER.debug(errorMessage);
            throw new ResourceNotFoundException(errorMessage);
        }
    }
}
