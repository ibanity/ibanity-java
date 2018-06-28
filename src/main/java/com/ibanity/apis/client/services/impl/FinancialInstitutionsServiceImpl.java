package com.ibanity.apis.client.services.impl;

import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.models.CustomerAccessToken;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.services.FinancialInstitutionsService;
import io.crnk.core.exception.CrnkMappableException;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import io.crnk.core.resource.list.ResourceList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

public class FinancialInstitutionsServiceImpl extends AbstractServiceImpl implements FinancialInstitutionsService {
    private static final Logger LOGGER = LogManager.getLogger(FinancialInstitutionsServiceImpl.class);

    public FinancialInstitutionsServiceImpl() {
        super();
    }

    @Override
    public ResourceList<FinancialInstitution> getFinancialInstitutions(final CustomerAccessToken customerAccessToken) {
        return getFinancialInstitutions(customerAccessToken, new IbanityPagingSpec());
    }

    @Override
    public ResourceList<FinancialInstitution> getFinancialInstitutions(final CustomerAccessToken customerAccessToken, final IbanityPagingSpec pagingSpec) {
        String finalPath = IbanityConfiguration.getApiIUrls().getCustomer().getFinancialInstitutions().replace(FinancialInstitution.RESOURCE_PATH, "");
        return getFinancialInstitutions(getApiClient(finalPath, customerAccessToken).getRepositoryForType(FinancialInstitution.class), pagingSpec);
    }

    @Override
    public ResourceList<FinancialInstitution> getFinancialInstitutions() {
        return getFinancialInstitutions(new IbanityPagingSpec());
    }

    @Override
    public ResourceList<FinancialInstitution> getFinancialInstitutions(final IbanityPagingSpec pagingSpec) {
        return getFinancialInstitutions(getApiClient("").getRepositoryForType(FinancialInstitution.class), pagingSpec);
    }

    @Override
    public FinancialInstitution getFinancialInstitution(final UUID financialInstitutionId) throws ApiErrorsException {
        try {
            return getApiClient("").getRepositoryForType(FinancialInstitution.class).findOne(financialInstitutionId, new QuerySpec(FinancialInstitution.class));
        } catch (CrnkMappableException e) {
            LOGGER.debug(e.getErrorData().toString());
            throw new ApiErrorsException(e.getHttpStatus(), e.getErrorData(), e);
        }
    }

    protected ResourceList<FinancialInstitution> getFinancialInstitutions(final ResourceRepositoryV2<FinancialInstitution, UUID> financialInstitutionsRepo, final IbanityPagingSpec pagingSpec) {
        QuerySpec querySpec = new QuerySpec(FinancialInstitution.class);
        querySpec.setPagingSpec(pagingSpec);
        return findAll(querySpec, financialInstitutionsRepo);
    }
}
