package com.ibanity.apis.client.sandbox.services.impl;

import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.sandbox.services.SandboxFinancialInstitutionsService;
import com.ibanity.apis.client.services.impl.FinancialInstitutionsServiceImpl;
import io.crnk.core.repository.ResourceRepositoryV2;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class SandboxFinancialInstitutionsServiceImpl extends FinancialInstitutionsServiceImpl implements SandboxFinancialInstitutionsService {

    public SandboxFinancialInstitutionsServiceImpl() {
        super();
    }

    @Override
    public FinancialInstitution create(final String name) {
        return create(name, null);
    }

    @Override
    public FinancialInstitution create(final String name, final UUID idempotencyKey) {
        FinancialInstitution financialInstitution = new FinancialInstitution();
        financialInstitution.setSandbox(Boolean.TRUE);
        financialInstitution.setName(name);
        return getFinancialInstitutionsRepo(idempotencyKey).create(financialInstitution);
    }

    @Override
    public FinancialInstitution updateFinancialInstitution(final FinancialInstitution financialInstitution) {
        return getFinancialInstitutionsRepo(null).save(financialInstitution);
    }

    @Override
    public FinancialInstitution updateFinancialInstitution(final FinancialInstitution financialInstitution, final UUID idempotencyKey) {
        return getFinancialInstitutionsRepo(idempotencyKey).save(financialInstitution);
    }

    @Override
    public void deleteFinancialInstitution(final UUID financialInstitutionId) throws ApiErrorsException {
        getFinancialInstitutionsRepo(null).delete(financialInstitutionId);
    }
    protected ResourceRepositoryV2<FinancialInstitution, UUID> getFinancialInstitutionsRepo(final UUID idempotencyKey) {
        String finalPath = StringUtils.removeEnd(
                IbanityConfiguration.getApiIUrls().getSandbox().getFinancialInstitutions()
                .replace(FinancialInstitution.RESOURCE_PATH, "")
                .replace(FinancialInstitution.API_URL_TAG_ID, ""), "//");

        return getApiClient(finalPath, null, idempotencyKey).getRepositoryForType(FinancialInstitution.class);
    }
}
