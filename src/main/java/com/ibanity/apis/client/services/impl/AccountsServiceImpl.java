package com.ibanity.apis.client.services.impl;

import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.models.Account;
import com.ibanity.apis.client.models.AccountInformationAccessRequest;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.models.factory.read.AccountReadQuery;
import com.ibanity.apis.client.models.factory.read.AccountsReadQuery;
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
    public Account find(final AccountReadQuery accountReadQuery) {
        return getRepository(accountReadQuery.getCustomerAccessToken(), accountReadQuery.getFinancialInstitutionId(), null)
                .findOne(accountReadQuery.getAccountId(), new QuerySpec(Account.class));
    }

    @Override
    public ResourceList<Account> list(final AccountsReadQuery accountsReadQuery) {
        QuerySpec querySpec = new QuerySpec(Account.class);

        if (accountsReadQuery.getPagingSpec() != null) {
            querySpec.setPagingSpec(accountsReadQuery.getPagingSpec());
        } else {
            querySpec.setPagingSpec(IbanityPagingSpec.DEFAULT_PAGING_SPEC);
        }

        return findAll(querySpec,
                getRepository(accountsReadQuery.getCustomerAccessToken(),
                        accountsReadQuery.getFinancialInstitutionId(),
                        accountsReadQuery.getAccountInformationAccessRequestId()));
    }

    private ResourceRepositoryV2<Account, UUID> getRepository(
            final String customerAccessToken, final UUID financialInstitutionId, final UUID accountInformationAccessRequestId) {

        String finalPath;
        if (accountInformationAccessRequestId != null) {
            finalPath = IbanityConfiguration.getApiUrls().getCustomer().getFinancialInstitution().getAccountInformationAccessRequest().getAccounts()
                    .replace(FinancialInstitution.API_URL_TAG_ID, financialInstitutionId.toString())
                    .replace(AccountInformationAccessRequest.API_URL_TAG_ID, financialInstitutionId.toString());
        } else if (financialInstitutionId != null) {
            finalPath = IbanityConfiguration.getApiUrls().getCustomer().getFinancialInstitution().getAccounts()
                    .replace(FinancialInstitution.API_URL_TAG_ID, financialInstitutionId.toString());
        } else {
            finalPath = IbanityConfiguration.getApiUrls().getCustomer().getAccounts();
        }
        finalPath = finalPath.replace(Account.RESOURCE_PATH, "")
                .replace(Account.API_URL_TAG_ID, "");
        while (finalPath.endsWith("/")) {
            finalPath = StringUtils.removeEnd(finalPath, "/");
        }

        return getApiClient(finalPath, customerAccessToken).getRepositoryForType(Account.class);
    }
}
