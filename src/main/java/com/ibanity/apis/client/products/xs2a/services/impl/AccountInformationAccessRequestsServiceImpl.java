package com.ibanity.apis.client.products.xs2a.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.DataApiModel;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.mappers.IbanityModelMapper;
import com.ibanity.apis.client.models.IbanityModel;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.xs2a.models.AccountInformationAccessRequestMeta;
import com.ibanity.apis.client.products.xs2a.models.AuthorizationPortal;
import com.ibanity.apis.client.products.xs2a.models.FinancialInstitution;
import com.ibanity.apis.client.products.xs2a.models.create.AccountInformationAccessRequestCreationQuery;
import com.ibanity.apis.client.products.xs2a.models.create.AuthorizationPortalCreationQuery;
import com.ibanity.apis.client.products.xs2a.models.links.AccountInformationAccessLinks;
import com.ibanity.apis.client.products.xs2a.models.links.AccountLinks;
import com.ibanity.apis.client.products.xs2a.services.AccountInformationAccessRequestsService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import lombok.*;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
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
    public com.ibanity.apis.client.products.xs2a.models.AccountInformationAccessRequest create(AccountInformationAccessRequestCreationQuery accountInformationAccessRequestCreationQuery) {

        String financialInstitutionId = accountInformationAccessRequestCreationQuery.getFinancialInstitutionId().toString();

        URI uri = getUri(financialInstitutionId, "");

        AccountInformationAccessRequest ibanityModel = mapAttributes(accountInformationAccessRequestCreationQuery);
        AccountInformationAccessRequestMeta meta = mapMeta(accountInformationAccessRequestCreationQuery);
        RequestApiModel request = buildRequest(AccountInformationAccessRequest.RESOURCE_TYPE, ibanityModel, meta);
        String response = ibanityHttpClient.post(uri, request, accountInformationAccessRequestCreationQuery.getAdditionalHeaders(), accountInformationAccessRequestCreationQuery.getCustomerAccessToken());
        return IbanityModelMapper.mapResource(response, responseMapping());
    }

    @Override
    public com.ibanity.apis.client.products.xs2a.models.AccountInformationAccessRequest find(AccountInformationAccessRequestCreationQuery accountInformationAccessRequestCreationQuery) {
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
                        .replace(com.ibanity.apis.client.products.xs2a.models.AccountInformationAccessRequest.API_URL_TAG_ID, accountInformationAccessRequestId),
                "/"));
    }

    private Function<DataApiModel, com.ibanity.apis.client.products.xs2a.models.AccountInformationAccessRequest> responseMapping() {
        return dataApiModel -> {
            com.ibanity.apis.client.products.xs2a.models.AccountInformationAccessRequest aiar = IbanityModelMapper.toIbanityModel(dataApiModel, com.ibanity.apis.client.products.xs2a.models.AccountInformationAccessRequest.class);

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

    private AccountInformationAccessRequest mapAttributes(AccountInformationAccessRequestCreationQuery creationQuery) {
        List<String> allowedAccountSubtypes = creationQuery.getAllowedAccountSubtypes();
        return AccountInformationAccessRequest.builder()
                .redirectUri(creationQuery.getRedirectUri())
                .consentReference(creationQuery.getConsentReference())
                .requestedAccountReferences(creationQuery.getRequestedAccountReferences())
                .locale(creationQuery.getLocale())
                .customerIpAddress(creationQuery.getCustomerIpAddress())
                .allowFinancialInstitutionRedirectUri(creationQuery.isAllowFinancialInstitutionRedirectUri())
                .allowedAccountSubtypes(allowedAccountSubtypes.isEmpty() ? null : allowedAccountSubtypes)
                .skipIbanityCompletionCallback(creationQuery.isSkipIbanityCompletionCallback())
                .build();
    }

    private AccountInformationAccessRequestMeta mapMeta(AccountInformationAccessRequestCreationQuery creationQuery) {
        if(creationQuery.getMetaRequestCreationQuery() == null || creationQuery.getMetaRequestCreationQuery().getAuthorizationPortalCreationQuery() == null) {
            return null;
        } else {
            AuthorizationPortalCreationQuery authorizationPortalCreationQuery = creationQuery.getMetaRequestCreationQuery().getAuthorizationPortalCreationQuery();
            return AccountInformationAccessRequestMeta.builder()
                    .authorizationPortal(AuthorizationPortal.builder()
                            .disclaimerContent(authorizationPortalCreationQuery.getDisclaimerContent())
                            .disclaimerTitle(authorizationPortalCreationQuery.getDisclaimerTitle())
                            .financialInstitutionPrimaryColor(authorizationPortalCreationQuery.getFinancialInstitutionPrimaryColor())
                            .financialInstitutionSecondaryColor(authorizationPortalCreationQuery.getFinancialInstitutionSecondaryColor())
                            .build())
                    .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class AccountInformationAccessRequest implements IbanityModel {

        public static final String RESOURCE_TYPE = "accountInformationAccessRequest";
        public static final String API_URL_TAG_ID = "{" + RESOURCE_TYPE + URL_PARAMETER_ID_POSTFIX + "}";

        private UUID id;
        private String selfLink;

        private String consentReference;
        private String redirectUri;
        private String status;
        private String locale;
        private String customerIpAddress;
        private boolean allowFinancialInstitutionRedirectUri;
        private boolean skipIbanityCompletionCallback;

        @Builder.Default
        private List<String> requestedAccountReferences = Collections.emptyList();

        @Builder.Default
        private List<String> allowedAccountSubtypes = Collections.emptyList();
    }
}
