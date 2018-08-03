package com.ibanity.apis.client.services.impl;

import com.ibanity.apis.client.configuration.IbanityConfiguration;
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
        return list(getRepository(customerAccessToken), pagingSpec);
    }

    @Override
    public ResourceList<FinancialInstitution> list() {
        return list(new IbanityPagingSpec());
    }

    @Override
    public ResourceList<FinancialInstitution> list(final IbanityPagingSpec pagingSpec) {
        return list(getRepository(), pagingSpec);
    }

    @Override
    public FinancialInstitution find(final UUID financialInstitutionId) {
        return getRepository().findOne(financialInstitutionId, new QuerySpec(FinancialInstitution.class));
    }

    private ResourceList<FinancialInstitution> list(
            final ResourceRepositoryV2<FinancialInstitution, UUID> repository,
            final IbanityPagingSpec pagingSpec) {

        QuerySpec querySpec = new QuerySpec(FinancialInstitution.class).setPagingSpec(pagingSpec);

        return repository.findAll(querySpec);
    }

    private ResourceRepositoryV2<FinancialInstitution, UUID> getRepository() {
        String finalPath = StringUtils.removeEnd(
                IbanityConfiguration.getApiUrls().getFinancialInstitutions()
                        .replace(FinancialInstitution.RESOURCE_PATH, "")
                        .replace(FinancialInstitution.API_URL_TAG_ID, ""),
                "//");

        return getApiClient(finalPath).getRepositoryForType(FinancialInstitution.class);
    }

    private ResourceRepositoryV2<FinancialInstitution, UUID> getRepository(final String customerAccessToken) {
        String finalPath = IbanityConfiguration.getApiUrls().getCustomer().getFinancialInstitutions()
                .replace(FinancialInstitution.RESOURCE_PATH, "");

        return getApiClient(finalPath, customerAccessToken).getRepositoryForType(FinancialInstitution.class);
    }
}
