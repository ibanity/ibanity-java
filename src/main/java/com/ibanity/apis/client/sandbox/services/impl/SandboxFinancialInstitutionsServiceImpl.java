package com.ibanity.apis.client.sandbox.services.impl;

import com.ibanity.apis.client.configuration.IbanityConfiguration;
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
        return getRepository(idempotencyKey).create(financialInstitution);
    }

    @Override
    public FinancialInstitution update(final FinancialInstitution financialInstitution) {
        return getRepository(null).save(financialInstitution);
    }

    @Override
    public FinancialInstitution update(final FinancialInstitution financialInstitution, final UUID idempotencyKey) {
        return getRepository(idempotencyKey).save(financialInstitution);
    }

    @Override
    public void delete(final UUID financialInstitutionId) {
        getRepository().delete(financialInstitutionId);
    }

    private ResourceRepositoryV2<FinancialInstitution, UUID> getRepository() {
        String finalPath = StringUtils.removeEnd(
                IbanityConfiguration.getApiUrls().getSandbox().getFinancialInstitutions()
                        .replace(FinancialInstitution.RESOURCE_PATH, "")
                        .replace(FinancialInstitution.API_URL_TAG_ID, ""), "//");

        return getApiClient(finalPath, null, null).getRepositoryForType(FinancialInstitution.class);
    }

    private ResourceRepositoryV2<FinancialInstitution, UUID> getRepository(final UUID idempotencyKey) {
        String finalPath = StringUtils.removeEnd(
                IbanityConfiguration.getApiUrls().getSandbox().getFinancialInstitutions()
                .replace(FinancialInstitution.RESOURCE_PATH, "")
                .replace(FinancialInstitution.API_URL_TAG_ID, ""), "//");

        return getApiClient(finalPath, null, idempotencyKey).getRepositoryForType(FinancialInstitution.class);
    }
}
