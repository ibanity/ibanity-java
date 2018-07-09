package com.ibanity.apis.client.services.impl;

import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.models.AccountInformationAccessRequest;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.services.AccountInformationAccessRequestsService;
import io.crnk.core.repository.ResourceRepositoryV2;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class AccountInformationAccessRequestsServiceImpl extends AbstractServiceImpl implements AccountInformationAccessRequestsService {

    public AccountInformationAccessRequestsServiceImpl() {
        super();
    }

    @Override
    public AccountInformationAccessRequest createForFinancialInstitution(final String customerAccessToken, final UUID financialInstitutionId, final String redirectURI, final String consentReference) {
        return createForFinancialInstitution(customerAccessToken, financialInstitutionId, redirectURI, consentReference, null);
    }

    @Override
    public AccountInformationAccessRequest createForFinancialInstitution(final String customerAccessToken, final UUID financialInstitutionId, final String redirectURI, final String consentReference, final UUID idempotencyKey) {
        AccountInformationAccessRequest accountInformationAccessRequest = new AccountInformationAccessRequest();
        accountInformationAccessRequest.setRedirectUri(redirectURI);
        accountInformationAccessRequest.setConsentReference(consentReference);
        return getAccounsInformationAccessRequestsRepo(customerAccessToken, financialInstitutionId, idempotencyKey).create(accountInformationAccessRequest);
    }

    protected ResourceRepositoryV2<AccountInformationAccessRequest, UUID> getAccounsInformationAccessRequestsRepo(final String customerAccessToken, final UUID financialInstitutionId, final UUID idempotencyKey) {
        String finalPath = StringUtils.removeEnd(
                IbanityConfiguration.getApiIUrls().getCustomer().getFinancialInstitution().getAccountInformationAccessRequests()
                        .replace(FinancialInstitution.API_URL_TAG_ID, financialInstitutionId.toString())
                        .replace(AccountInformationAccessRequest.RESOURCE_PATH, "")
                        .replace(AccountInformationAccessRequest.API_URL_TAG_ID, ""), "//");

        return getApiClient(finalPath, customerAccessToken, idempotencyKey).getRepositoryForType(AccountInformationAccessRequest.class);
    }
}