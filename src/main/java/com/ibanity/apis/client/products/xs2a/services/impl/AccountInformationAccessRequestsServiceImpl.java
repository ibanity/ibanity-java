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
import com.ibanity.apis.client.products.xs2a.models.create.MetaRequestCreationQuery;
import com.ibanity.apis.client.products.xs2a.models.links.AccountInformationAccessLinks;
import com.ibanity.apis.client.products.xs2a.models.links.AccountLinks;
import com.ibanity.apis.client.products.xs2a.models.links.InitialAccountTransactionsSynchronizationsLinks;
import com.ibanity.apis.client.products.xs2a.models.read.AccountInformationAccessRequestReadQuery;
import com.ibanity.apis.client.products.xs2a.services.AccountInformationAccessRequestsService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.http.HttpResponse;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static com.ibanity.apis.client.mappers.ModelMapperHelper.buildRequest;
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
        HttpResponse response = ibanityHttpClient.post(uri, request, accountInformationAccessRequestCreationQuery.getAdditionalHeaders(), accountInformationAccessRequestCreationQuery.getCustomerAccessToken());
        return IbanityModelMapper.mapResource(response, responseMapping());
    }

    @Deprecated
    @Override
    public com.ibanity.apis.client.products.xs2a.models.AccountInformationAccessRequest find(AccountInformationAccessRequestCreationQuery accountInformationAccessRequestCreationQuery) {
        return find(AccountInformationAccessRequestReadQuery.builder()
                .accountInformationAccessRequestId(accountInformationAccessRequestCreationQuery.getAccountInformationAccessRequestId())
                .financialInstitutionId(accountInformationAccessRequestCreationQuery.getFinancialInstitutionId())
                .customerAccessToken(accountInformationAccessRequestCreationQuery.getCustomerAccessToken())
                .additionalHeaders(accountInformationAccessRequestCreationQuery.getAdditionalHeaders())
                .build());
    }

    @Override
    public com.ibanity.apis.client.products.xs2a.models.AccountInformationAccessRequest find(AccountInformationAccessRequestReadQuery accountInformationAccessRequestReadQuery) {
        String financialInstitutionId = accountInformationAccessRequestReadQuery.getFinancialInstitutionId().toString();
        String resourceId = accountInformationAccessRequestReadQuery.getAccountInformationAccessRequestId().toString();

        URI uri = getUri(financialInstitutionId, resourceId);

        HttpResponse response = ibanityHttpClient.get(uri, accountInformationAccessRequestReadQuery.getAdditionalHeaders(), accountInformationAccessRequestReadQuery.getCustomerAccessToken());
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

            if (dataApiModel.getRelationships() != null) {
                if (dataApiModel.getRelationships().get("accounts") != null
                        && dataApiModel.getRelationships().get("accounts").getLinks() != null) {
                    aiar.setAccountLinks(AccountLinks.builder()
                            .related(dataApiModel.getRelationships().get("accounts").getLinks().getRelated())
                            .build());
                }
                if (dataApiModel.getRelationships().get("initialAccountTransactionsSynchronizations") != null
                        && dataApiModel.getRelationships().get("initialAccountTransactionsSynchronizations").getLinks() != null) {
                    aiar.setInitialAccountTransactionsSynchronizationsLinks(InitialAccountTransactionsSynchronizationsLinks.builder()
                            .related(dataApiModel.getRelationships().get("initialAccountTransactionsSynchronizations").getLinks().getRelated())
                            .build());
                }
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
                .allowMulticurrencyAccounts(creationQuery.isAllowMulticurrencyAccounts())
                .state(creationQuery.getState())
                .financialInstitutionCustomerReference(creationQuery.getFinancialInstitutionCustomerReference())
                .build();
    }

    private AccountInformationAccessRequestMeta mapMeta(AccountInformationAccessRequestCreationQuery creationQuery) {
        MetaRequestCreationQuery metaRequestCreationQuery = creationQuery.getMetaRequestCreationQuery();
        if (metaRequestCreationQuery == null) {
            return null;
        } else {
            AuthorizationPortalCreationQuery authorizationPortalCreationQuery = metaRequestCreationQuery.getAuthorizationPortalCreationQuery();
            AuthorizationPortal authorizationPortal;
            if (metaRequestCreationQuery.getAuthorizationPortalCreationQuery() == null) {
                authorizationPortal = null;
            } else {
                authorizationPortal = AuthorizationPortal.builder()
                        .disclaimerContent(authorizationPortalCreationQuery.getDisclaimerContent())
                        .disclaimerTitle(authorizationPortalCreationQuery.getDisclaimerTitle())
                        .financialInstitutionPrimaryColor(authorizationPortalCreationQuery.getFinancialInstitutionPrimaryColor())
                        .financialInstitutionSecondaryColor(authorizationPortalCreationQuery.getFinancialInstitutionSecondaryColor())
                        .build();
            }
            return AccountInformationAccessRequestMeta.builder()
                    .requestedPastTransactionDays(metaRequestCreationQuery.getRequestedPastTransactionDays())
                    .authorizationPortal(authorizationPortal)
                    .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    static class AccountInformationAccessRequest implements IbanityModel {

        public static final String RESOURCE_TYPE = "accountInformationAccessRequest";
        public static final String API_URL_TAG_ID = "{" + RESOURCE_TYPE + URL_PARAMETER_ID_POSTFIX + "}";

        private UUID id;
        private String selfLink;
        private String requestId;

        private String consentReference;
        private String redirectUri;
        private String status;
        private String locale;
        private String customerIpAddress;
        private boolean allowFinancialInstitutionRedirectUri;
        private boolean skipIbanityCompletionCallback;
        private boolean allowMulticurrencyAccounts;
        private String state;
        private String financialInstitutionCustomerReference;

        @Builder.Default
        private List<String> requestedAccountReferences = Collections.emptyList();

        @Builder.Default
        private List<String> allowedAccountSubtypes = Collections.emptyList();
    }
}
