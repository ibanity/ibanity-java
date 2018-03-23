package com.ibanity.client.api.impl;

import com.ibanity.client.api.FinancialInstitutionsService;
import com.ibanity.client.exceptions.ResourceNotFoundException;
import com.ibanity.client.models.FinancialInstitution;
import com.ibanity.client.paging.PagingSpec;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import io.crnk.core.resource.list.ResourceList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

public class FinancialInstitutionsServiceImpl extends AbstractServiceImpl implements FinancialInstitutionsService{
    private static final Logger LOGGER = LogManager.getLogger(FinancialInstitutionsServiceImpl.class);

    private final ResourceRepositoryV2<FinancialInstitution, UUID> financialInstitutionsRepo;

    public FinancialInstitutionsServiceImpl() {
        super();
        financialInstitutionsRepo = getApiClient("/").getRepositoryForType(FinancialInstitution.class);
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
}
