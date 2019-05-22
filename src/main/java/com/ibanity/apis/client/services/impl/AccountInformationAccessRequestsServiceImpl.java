package com.ibanity.apis.client.services.impl;

import com.ibanity.apis.client.jsonapi.DataApiModel;
import com.ibanity.apis.client.mappers.IbanityModelMapper;
import com.ibanity.apis.client.models.AccountInformationAccessRequest;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.models.IbanityModel;
import com.ibanity.apis.client.models.factory.create.AccountInformationAccessRequestCreationQuery;
import com.ibanity.apis.client.models.links.AccountInformationAccessLinks;
import com.ibanity.apis.client.models.links.AccountLinks;
import com.ibanity.apis.client.network.http.client.IbanityHttpClient;
import com.ibanity.apis.client.services.AccountInformationAccessRequestsService;
import com.ibanity.apis.client.services.ApiUrlProvider;

import java.net.URI;
import java.util.function.Function;

import static com.ibanity.apis.client.utils.URIHelper.buildUri;
import static org.apache.commons.lang3.StringUtils.removeEnd;

public class AccountInformationAccessRequestsServiceImpl implements AccountInformationAccessRequestsService {

    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public AccountInformationAccessRequestsServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public AccountInformationAccessRequest create(
            final AccountInformationAccessRequestCreationQuery accountInformationAccessRequestCreationQuery) {

        String financialInstitutionId = accountInformationAccessRequestCreationQuery.getFinancialInstitutionId().toString();

        URI uri = getUri(financialInstitutionId, "");

        String response = ibanityHttpClient.post(uri, accountInformationAccessRequestCreationQuery.getCustomerAccessToken(), mapRequest(accountInformationAccessRequestCreationQuery));
        return IbanityModelMapper.mapResource(response, responseMapping());
    }

    @Override
    public AccountInformationAccessRequest find(AccountInformationAccessRequestCreationQuery accountInformationAccessRequestCreationQuery) {
        String financialInstitutionId = accountInformationAccessRequestCreationQuery.getFinancialInstitutionId().toString();
        String resourceId = accountInformationAccessRequestCreationQuery.getAccountInformationAccessRequestId().toString();

        URI uri = getUri(financialInstitutionId, resourceId);

        String response = ibanityHttpClient.get(uri, accountInformationAccessRequestCreationQuery.getCustomerAccessToken());
        return IbanityModelMapper.mapResource(response, AccountInformationAccessRequest.class);
    }

    private URI getUri(String financialInstitutionId, String accountInformationAccessRequestId) {
        String url = apiUrlProvider.find("customer", "financialInstitution", "accountInformationAccessRequests");
        return buildUri(removeEnd(
                url
                        .replace(FinancialInstitution.API_URL_TAG_ID, financialInstitutionId)
                        .replace(AccountInformationAccessRequest.API_URL_TAG_ID, accountInformationAccessRequestId),
                "/"));
    }

    private Function<DataApiModel, AccountInformationAccessRequest> responseMapping() {
        return dataApiModel -> {
            AccountInformationAccessRequest aiar = IbanityModelMapper.toIbanityModel(dataApiModel, AccountInformationAccessRequest.class);

            aiar.setAccountInformationAccessLinks(AccountInformationAccessLinks.builder()
                    .redirect(dataApiModel.getLinks() == null ? null : dataApiModel.getLinks().getRedirect())
                    .build());

            if (dataApiModel.getRelationships() != null
                    && dataApiModel.getRelationships().get("accounts") != null
                    && dataApiModel.getRelationships().get("accounts").getLinks() != null) {
                aiar.setAccountLinks(AccountLinks.builder()
                        .related(dataApiModel.getRelationships().get("accounts").getLinks().getRelated())
                        .build());
            }

            return aiar;
        };
    }

    private IbanityModel mapRequest(AccountInformationAccessRequestCreationQuery creationQuery) {
        return AccountInformationAccessRequest.builder()
                .redirectUri(creationQuery.getRedirectURI())
                .consentReference(creationQuery.getConsentReference())
                .requestedAccountReferences(creationQuery.getRequestedAccountReferences())
                .locale(creationQuery.getLocale())
                .customerIpAddress(creationQuery.getCustomerIpAddress())
                .build();
    }
}
