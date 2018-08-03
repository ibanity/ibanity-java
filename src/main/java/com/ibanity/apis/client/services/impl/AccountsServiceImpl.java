package com.ibanity.apis.client.services.impl;

import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.models.Account;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.services.AccountsService;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import io.crnk.core.resource.list.ResourceList;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class AccountsServiceImpl extends AbstractServiceImpl implements AccountsService {

    public AccountsServiceImpl() {
        super();
    }

    @Override
    public Account find(final String customerAccessToken, final UUID accountId, final UUID financialInstitutionId) {
        return getRepository(customerAccessToken, financialInstitutionId).findOne(accountId, new QuerySpec(Account.class));
    }

    @Override
    public ResourceList<Account> list(final String customerAccessToken) {
        return list(customerAccessToken, new IbanityPagingSpec());
    }

    @Override
    public ResourceList<Account> list(final String customerAccessToken, final IbanityPagingSpec pagingSpec) {
        QuerySpec querySpec = new QuerySpec(Account.class);
        querySpec.setPagingSpec(pagingSpec);
        return findAll(querySpec, getRepository(customerAccessToken));
    }

    @Override
    public ResourceList<Account> list(final String customerAccessToken, final UUID financialInstitutionId) {
        return list(customerAccessToken, financialInstitutionId, new IbanityPagingSpec());
    }

    @Override
    public ResourceList<Account> list(
            final String customerAccessToken, final UUID financialInstitutionId, final IbanityPagingSpec pagingSpec) {
        QuerySpec querySpec = new QuerySpec(Account.class);
        querySpec.setPagingSpec(pagingSpec);
        return findAll(querySpec, getRepository(customerAccessToken, financialInstitutionId));
    }

    private ResourceRepositoryV2<Account, UUID> getRepository(
            final String customerAccessToken, final UUID financialInstitutionId) {

        String finalPath = StringUtils.removeEnd(
                IbanityConfiguration.getApiUrls().getCustomer().getFinancialInstitution().getAccounts()
                        .replace(FinancialInstitution.API_URL_TAG_ID, financialInstitutionId.toString())
                        .replace(Account.RESOURCE_PATH, "")
                        .replace(Account.API_URL_TAG_ID, ""),
                "//");

        return getApiClient(finalPath, customerAccessToken).getRepositoryForType(Account.class);
    }

    private ResourceRepositoryV2<Account, UUID> getRepository(final String customerAccessToken) {
        String finalPath = IbanityConfiguration.getApiUrls().getCustomer().getAccounts()
                        .replace(Account.RESOURCE_PATH, "");

        return getApiClient(finalPath, customerAccessToken).getRepositoryForType(Account.class);
    }
}
