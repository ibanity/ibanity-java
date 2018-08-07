package com.ibanity.apis.client.services.impl;

import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.models.AccountInformationAccessRequest;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.models.factory.create.AccountInformationAccessRequestCreationQuery;
import com.ibanity.apis.client.services.AccountInformationAccessRequestsService;
import io.crnk.core.repository.ResourceRepositoryV2;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class AccountInformationAccessRequestsServiceImpl extends AbstractServiceImpl implements AccountInformationAccessRequestsService {

    public AccountInformationAccessRequestsServiceImpl() {
        super();
    }

    @Override
    public AccountInformationAccessRequest create(
            final AccountInformationAccessRequestCreationQuery accountInformationAccessRequestCreationQuery) {

        AccountInformationAccessRequest accountInformationAccessRequest = new AccountInformationAccessRequest();
        accountInformationAccessRequest.setRedirectUri(accountInformationAccessRequestCreationQuery.getRedirectURI());
        accountInformationAccessRequest.setConsentReference(accountInformationAccessRequestCreationQuery.getConsentReference());

        return getRepository(
                accountInformationAccessRequestCreationQuery.getCustomerAccessToken(),
                accountInformationAccessRequestCreationQuery.getFinancialInstitutionId(),
                accountInformationAccessRequestCreationQuery.getIdempotencyKey())
                .create(accountInformationAccessRequest);
    }

    private ResourceRepositoryV2<AccountInformationAccessRequest, UUID> getRepository(
            final String customerAccessToken, final UUID financialInstitutionId, final UUID idempotencyKey) {
        String finalPath = StringUtils.removeEnd(
                IbanityConfiguration.getApiUrls().getCustomer().getFinancialInstitution().getAccountInformationAccessRequests()
                        .replace(FinancialInstitution.API_URL_TAG_ID, financialInstitutionId.toString())
                        .replace(AccountInformationAccessRequest.RESOURCE_PATH, "")
                        .replace(AccountInformationAccessRequest.API_URL_TAG_ID, ""), "//");

        return getApiClient(finalPath, customerAccessToken, idempotencyKey).getRepositoryForType(AccountInformationAccessRequest.class);
    }
}
