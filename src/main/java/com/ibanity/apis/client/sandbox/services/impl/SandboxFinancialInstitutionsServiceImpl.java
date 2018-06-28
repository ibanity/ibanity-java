package com.ibanity.apis.client.sandbox.services.impl;

import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.sandbox.services.SandboxFinancialInstitutionsService;
import com.ibanity.apis.client.services.impl.FinancialInstitutionsServiceImpl;
import io.crnk.core.exception.CrnkMappableException;
import io.crnk.core.repository.ResourceRepositoryV2;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

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
    public void deleteFinancialInstitution(final UUID financialInstitutionId) throws ApiErrorsException {
        try {
            getFinancialInstitutionsRepo(null).delete(financialInstitutionId);
        } catch (CrnkMappableException e) {
            LOGGER.debug(e.getErrorData().toString());
            throw new ApiErrorsException(e.getHttpStatus(), e.getErrorData(), e);
        }
    }
    protected ResourceRepositoryV2<FinancialInstitution, UUID> getFinancialInstitutionsRepo(final UUID idempotency) {
        String finalPath = StringUtils.removeEnd(
                IbanityConfiguration.getApiIUrls().getSandbox().getFinancialInstitutions()
                .replace(FinancialInstitution.RESOURCE_PATH, "")
                .replace(FinancialInstitution.API_URL_TAG_ID, "")
                ,"//");

        return getApiClient(finalPath, null, idempotency).getRepositoryForType(FinancialInstitution.class);
    }
}
