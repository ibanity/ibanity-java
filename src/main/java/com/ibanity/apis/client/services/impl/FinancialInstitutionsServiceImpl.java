package com.ibanity.apis.client.services.impl;

import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.services.FinancialInstitutionsService;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import io.crnk.core.resource.list.ResourceList;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class FinancialInstitutionsServiceImpl extends AbstractServiceImpl implements FinancialInstitutionsService {

    public FinancialInstitutionsServiceImpl() {
        super();
    }

    @Override
    public ResourceList<FinancialInstitution> list(final String customerAccessToken) {
        return list(customerAccessToken, new IbanityPagingSpec());
    }

    @Override
    public ResourceList<FinancialInstitution> list(final String customerAccessToken, final IbanityPagingSpec pagingSpec) {
        return list(getCustomerFinancialInstitutionsRepo(customerAccessToken), pagingSpec);
    }

    @Override
    public ResourceList<FinancialInstitution> list() {
        return list(new IbanityPagingSpec());
    }

    @Override
    public ResourceList<FinancialInstitution> list(final IbanityPagingSpec pagingSpec) {
        return list(getFinancialInstitutionsRepo(), pagingSpec);

    }

    @Override
    public FinancialInstitution find(final UUID financialInstitutionId) throws ApiErrorsException {
        return getFinancialInstitutionsRepo().findOne(financialInstitutionId, new QuerySpec(FinancialInstitution.class));
    }

    protected ResourceRepositoryV2<FinancialInstitution, UUID> getFinancialInstitutionsRepo() {
        String finalPath = StringUtils.removeEnd(
                IbanityConfiguration.getApiIUrls().getFinancialInstitutions()
                        .replace(FinancialInstitution.RESOURCE_PATH, "")
                        .replace(FinancialInstitution.API_URL_TAG_ID, ""), "//");

        return getApiClient(finalPath).getRepositoryForType(FinancialInstitution.class);
    }

    protected ResourceRepositoryV2<FinancialInstitution, UUID> getCustomerFinancialInstitutionsRepo(final String customerAccessToken) {
        String finalPath = IbanityConfiguration.getApiIUrls().getCustomer().getFinancialInstitutions().replace(FinancialInstitution.RESOURCE_PATH, "");

        return getApiClient(finalPath, customerAccessToken).getRepositoryForType(FinancialInstitution.class);
    }

    protected ResourceList<FinancialInstitution> list(final ResourceRepositoryV2<FinancialInstitution, UUID> financialInstitutionsRepo, final IbanityPagingSpec pagingSpec) {
        QuerySpec querySpec = new QuerySpec(FinancialInstitution.class);
        querySpec.setPagingSpec(pagingSpec);
        return findAll(querySpec, financialInstitutionsRepo);
    }
}
