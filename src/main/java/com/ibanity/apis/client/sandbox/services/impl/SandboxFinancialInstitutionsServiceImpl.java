package com.ibanity.apis.client.sandbox.services.impl;

import com.ibanity.apis.client.exceptions.ResourceNotFoundException;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.sandbox.services.SandboxFinancialInstitutionsService;
import com.ibanity.apis.client.services.impl.FinancialInstitutionsServiceImpl;
import io.crnk.core.repository.ResourceRepositoryV2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

import static com.ibanity.apis.client.services.configuration.IbanityConfiguration.FORWARD_SLASH;
import static com.ibanity.apis.client.services.configuration.IbanityConfiguration.SANBOX_PREFIX_PATH;

public class SandboxFinancialInstitutionsServiceImpl extends FinancialInstitutionsServiceImpl implements SandboxFinancialInstitutionsService {
    private static final Logger LOGGER = LogManager.getLogger(SandboxFinancialInstitutionsServiceImpl.class);

    private final ResourceRepositoryV2<FinancialInstitution, UUID> sandboxFinancialInstitutionsRepo;

    public SandboxFinancialInstitutionsServiceImpl() {
        super();
        sandboxFinancialInstitutionsRepo = getApiClient(SANBOX_PREFIX_PATH + FORWARD_SLASH).getRepositoryForType(FinancialInstitution.class);
    }


    @Override
    public FinancialInstitution createFinancialInstitution(FinancialInstitution financialInstitution) {
        financialInstitution.setSandbox(Boolean.TRUE);
        return sandboxFinancialInstitutionsRepo.create(financialInstitution);
    }

    @Override
    public FinancialInstitution updateFinancialInstitution(FinancialInstitution financialInstitution) {
        return sandboxFinancialInstitutionsRepo.save(financialInstitution);
    }

    @Override
    public void deleteFinancialInstitution(UUID financialInstitutionId) throws ResourceNotFoundException {
        try {
            sandboxFinancialInstitutionsRepo.delete(financialInstitutionId);
        } catch (io.crnk.core.exception.ResourceNotFoundException e) {
            String errorMessage = "Resource with ID:"+financialInstitutionId+": not found";
            LOGGER.debug(errorMessage);
            throw new ResourceNotFoundException(errorMessage);
        }
    }
}
