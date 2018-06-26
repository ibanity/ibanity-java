package com.ibanity.apis.client.services.impl;

import com.ibanity.apis.client.exceptions.ResourceNotFoundException;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.services.FinancialInstitutionsService;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import io.crnk.core.resource.list.ResourceList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

import static com.ibanity.apis.client.services.configuration.IbanityConfiguration.FORWARD_SLASH;

public class FinancialInstitutionsServiceImpl extends AbstractServiceImpl implements FinancialInstitutionsService {
    private static final Logger LOGGER = LogManager.getLogger(FinancialInstitutionsServiceImpl.class);

    private final ResourceRepositoryV2<FinancialInstitution, UUID> financialInstitutionsRepo;

    public FinancialInstitutionsServiceImpl() {
        super();
        financialInstitutionsRepo = getApiClient(FORWARD_SLASH).getRepositoryForType(FinancialInstitution.class);
    }

    @Override
    public ResourceList<FinancialInstitution> getFinancialInstitutions() {
        return getFinancialInstitutions(new IbanityPagingSpec());
    }

    @Override
    public ResourceList<FinancialInstitution> getFinancialInstitutions(final IbanityPagingSpec pagingSpec) {
        QuerySpec querySpec = new QuerySpec(FinancialInstitution.class);
        querySpec.setPagingSpec(pagingSpec);
        return findAll(querySpec, financialInstitutionsRepo);
    }

    @Override
    public FinancialInstitution getFinancialInstitution(final UUID financialInstitutionId) throws ResourceNotFoundException {
        try {
            return financialInstitutionsRepo.findOne(financialInstitutionId, new QuerySpec(FinancialInstitution.class));
        } catch (io.crnk.core.exception.ResourceNotFoundException e) {
            String errorMessage = "Resource with ID:" + financialInstitutionId + ": not found";
            LOGGER.debug(errorMessage);
            throw new ResourceNotFoundException(errorMessage);
        }
    }
}
