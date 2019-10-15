package com.ibanity.apis.client.products.xs2a.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.DataApiModel;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.mappers.IbanityModelMapper;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.xs2a.models.AccountInformationAccessRequest;
import com.ibanity.apis.client.products.xs2a.models.AuthorizationPortal;
import com.ibanity.apis.client.products.xs2a.models.FinancialInstitution;
import com.ibanity.apis.client.products.xs2a.models.Meta;
import com.ibanity.apis.client.products.xs2a.models.create.AccountInformationAccessRequestCreationQuery;
import com.ibanity.apis.client.products.xs2a.models.links.AccountInformationAccessLinks;
import com.ibanity.apis.client.products.xs2a.models.links.AccountLinks;
import com.ibanity.apis.client.products.xs2a.services.AccountInformationAccessRequestsService;
import com.ibanity.apis.client.services.ApiUrlProvider;

import java.net.URI;
import java.util.function.Function;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.buildRequest;
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
            AccountInformationAccessRequestCreationQuery accountInformationAccessRequestCreationQuery) {

        String financialInstitutionId = accountInformationAccessRequestCreationQuery.getFinancialInstitutionId().toString();

        URI uri = getUri(financialInstitutionId, "");

        AccountInformationAccessRequest ibanityModel = mapRequest(accountInformationAccessRequestCreationQuery);
        RequestApiModel request = buildRequest(AccountInformationAccessRequest.RESOURCE_TYPE, ibanityModel);
        String response = ibanityHttpClient.post(uri, request, accountInformationAccessRequestCreationQuery.getAdditionalHeaders(), accountInformationAccessRequestCreationQuery.getCustomerAccessToken());
        return IbanityModelMapper.mapResource(response, responseMapping());
    }

    @Override
    public AccountInformationAccessRequest find(AccountInformationAccessRequestCreationQuery accountInformationAccessRequestCreationQuery) {
        String financialInstitutionId = accountInformationAccessRequestCreationQuery.getFinancialInstitutionId().toString();
        String resourceId = accountInformationAccessRequestCreationQuery.getAccountInformationAccessRequestId().toString();

        URI uri = getUri(financialInstitutionId, resourceId);

        String response = ibanityHttpClient.get(uri, accountInformationAccessRequestCreationQuery.getAdditionalHeaders(), accountInformationAccessRequestCreationQuery.getCustomerAccessToken());
        return IbanityModelMapper.mapResource(response, responseMapping());
    }

    private URI getUri(String financialInstitutionId, String accountInformationAccessRequestId) {
        String url = apiUrlProvider.find(IbanityProduct.Xs2a, "customer", "financialInstitution", "accountInformationAccessRequests");
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

    private AccountInformationAccessRequest mapRequest(AccountInformationAccessRequestCreationQuery creationQuery) {
        return AccountInformationAccessRequest.builder()
                .redirectUri(creationQuery.getRedirectUri())
                .consentReference(creationQuery.getConsentReference())
                .requestedAccountReferences(creationQuery.getRequestedAccountReferences())
                .locale(creationQuery.getLocale())
                .customerIpAddress(creationQuery.getCustomerIpAddress())
                .allowFinancialInstitutionRedirect(creationQuery.isAllowFinancialInstitutionRedirect())
                .meta(Meta.builder()
                        .authorizationPortal(AuthorizationPortal.builder()
                                .disclaimerContent(creationQuery.getMetaRequestCreationQuery().getAuthorizationPortalCreationQuery().getDisclaimerContent())
                                .disclaimerTitle(creationQuery.getMetaRequestCreationQuery().getAuthorizationPortalCreationQuery().getDisclaimerTitle())
                                .financialInstitutionPrimaryColor(creationQuery.getMetaRequestCreationQuery().getAuthorizationPortalCreationQuery().getFinancialInstitutionPrimaryColor())
                                .financialInstitutionSecondaryColor(creationQuery.getMetaRequestCreationQuery().getAuthorizationPortalCreationQuery().getFinancialInstitutionSecondaryColor())
                                .build())
                        .build())
                .build();
    }
}
