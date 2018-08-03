package com.ibanity.apis.client.services.impl;

import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.models.factory.read.FinancialInstitutionReadQuery;
import com.ibanity.apis.client.models.factory.read.FinancialInstitutionsReadQuery;
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
    public ResourceList<FinancialInstitution> list(final FinancialInstitutionsReadQuery financialInstitutionsReadQuery) {
        QuerySpec querySpec = new QuerySpec(FinancialInstitution.class);

        if (financialInstitutionsReadQuery.getPagingSpec() != null) {
            querySpec.setPagingSpec(financialInstitutionsReadQuery.getPagingSpec());
        } else {
            querySpec.setPagingSpec(IbanityPagingSpec.DEFAULT_PAGING_SPEC);
        }

        return getRepository(financialInstitutionsReadQuery.getCustomerAccessToken())
                .findAll(querySpec);
    }

    @Override
    public FinancialInstitution find(final FinancialInstitutionReadQuery financialInstitutionReadQuery) {
        return getRepository(financialInstitutionReadQuery.getCustomerAccessToken())
                .findOne(financialInstitutionReadQuery.getFinancialInstitutionId(),
                new QuerySpec(FinancialInstitution.class));
    }

    private ResourceRepositoryV2<FinancialInstitution, UUID> getRepository(final String customerAccessToken) {
        String finalPath;

        if (customerAccessToken != null) {
            finalPath = IbanityConfiguration.getApiUrls().getCustomer().getFinancialInstitutions();
        } else {
            finalPath = IbanityConfiguration.getApiUrls().getFinancialInstitutions();
        }

        finalPath = finalPath
                .replace(FinancialInstitution.RESOURCE_PATH, "")
                .replace(FinancialInstitution.API_URL_TAG_ID, "");

        while (finalPath.endsWith("/")) {
            finalPath = StringUtils.removeEnd(finalPath, "/");
        }

        return getApiClient(finalPath, customerAccessToken).getRepositoryForType(FinancialInstitution.class);
    }
}
