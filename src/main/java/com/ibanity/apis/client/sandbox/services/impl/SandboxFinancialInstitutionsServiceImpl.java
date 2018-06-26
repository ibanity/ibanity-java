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

    public SandboxFinancialInstitutionsServiceImpl() {
        super();
    }

    @Override
    public FinancialInstitution createFinancialInstitution(final FinancialInstitution financialInstitution) {
        financialInstitution.setSandbox(Boolean.TRUE);
        return getFinancialInstitutionsRepo(null).create(financialInstitution);
    }

    @Override
    public FinancialInstitution createFinancialInstitution(final FinancialInstitution financialInstitution, final UUID idempotency) {
        financialInstitution.setSandbox(Boolean.TRUE);
        return getFinancialInstitutionsRepo(idempotency).create(financialInstitution);
    }

    @Override
    public FinancialInstitution updateFinancialInstitution(final FinancialInstitution financialInstitution) {
        return getFinancialInstitutionsRepo(null).save(financialInstitution);
    }

    @Override
    public FinancialInstitution updateFinancialInstitution(final FinancialInstitution financialInstitution, final UUID idempotency) {
        return getFinancialInstitutionsRepo(idempotency).save(financialInstitution);
    }

    @Override
    public void deleteFinancialInstitution(final UUID financialInstitutionId) throws ResourceNotFoundException {
        try {
            getFinancialInstitutionsRepo(null).delete(financialInstitutionId);
        } catch (io.crnk.core.exception.ResourceNotFoundException e) {
            String errorMessage = "Resource with ID:" + financialInstitutionId + ": not found";
            LOGGER.debug(errorMessage);
            throw new ResourceNotFoundException(errorMessage);
        }
    }
    protected ResourceRepositoryV2<FinancialInstitution, UUID> getFinancialInstitutionsRepo(final UUID idempotency) {
        return getApiClient(SANBOX_PREFIX_PATH + FORWARD_SLASH, null, idempotency).getRepositoryForType(FinancialInstitution.class);
    }
}
