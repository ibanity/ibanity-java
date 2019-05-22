package com.ibanity.apis.client.sandbox.services.impl;

import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.sandbox.models.factory.create.FinancialInstitutionCreationQuery;
import com.ibanity.apis.client.sandbox.models.factory.delete.FinancialInstitutionDeleteQuery;
import com.ibanity.apis.client.sandbox.models.factory.update.FinancialInstitutionUpdateQuery;
import com.ibanity.apis.client.sandbox.services.SandboxFinancialInstitutionsService;
import com.ibanity.apis.client.services.impl.FinancialInstitutionsServiceImpl;
import io.crnk.core.repository.ResourceRepositoryV2;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class SandboxFinancialInstitutionsServiceImpl extends FinancialInstitutionsServiceImpl implements SandboxFinancialInstitutionsService {

    public SandboxFinancialInstitutionsServiceImpl() {
        super(null, null);
    }

    @Override
    public FinancialInstitution create(
            final FinancialInstitutionCreationQuery financialInstitutionCreationQuery) {
        FinancialInstitution financialInstitution = new FinancialInstitution();
        financialInstitution.setSandbox(Boolean.TRUE);
        financialInstitution.setName(financialInstitutionCreationQuery.getName());
        return getRepository(financialInstitutionCreationQuery.getIdempotencyKey())
                .create(financialInstitution);
    }

    @Override
    public FinancialInstitution update(
            final FinancialInstitutionUpdateQuery financialInstitutionUpdateQuery) {
        FinancialInstitution financialInstitution = new FinancialInstitution();
        financialInstitution.setId(financialInstitutionUpdateQuery.getFinancialInstitutionId());
        financialInstitution.setName(financialInstitutionUpdateQuery.getName());

        return getRepository(financialInstitutionUpdateQuery.getIdempotencyKey())
                .save(financialInstitution);
    }

    @Override
    public void delete(final FinancialInstitutionDeleteQuery financialInstitutionDeleteQuery) {
        getRepository(financialInstitutionDeleteQuery.getIdempotencyKey())
                .delete(financialInstitutionDeleteQuery.getFinancialInstitutionId());
    }

    private ResourceRepositoryV2<FinancialInstitution, UUID> getRepository(final UUID idempotencyKey) {
        String finalPath = StringUtils.removeEnd(
                IbanityConfiguration.getApiUrls().getSandbox().getFinancialInstitutions()
                //.replace(FinancialInstitution.RESOURCE_PATH, "")
                .replace(FinancialInstitution.API_URL_TAG_ID, ""), "//");

        return null;
/*        return getApiClient(finalPath, null, idempotencyKey)
                .getRepositoryForType(FinancialInstitution.class);*/
    }
}
