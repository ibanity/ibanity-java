package com.ibanity.apis.client.products.xs2a.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.mappers.IbanityModelMapper;
import com.ibanity.apis.client.models.IbanityModel;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.xs2a.models.AccountInformationAccessRequest;
import com.ibanity.apis.client.products.xs2a.models.FinancialInstitution;
import com.ibanity.apis.client.products.xs2a.models.create.AccountInformationAccessRequestAuthorizationCreationQuery;
import com.ibanity.apis.client.products.xs2a.services.AccountInformationAccessRequestAuthorizationsService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.buildRequest;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;
import static java.util.Collections.emptyMap;

public class AccountInformationAccessRequestAuthorizationsServiceImpl implements AccountInformationAccessRequestAuthorizationsService {

    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public AccountInformationAccessRequestAuthorizationsServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public com.ibanity.apis.client.products.xs2a.models.AccountInformationAccessRequestAuthorization create(AccountInformationAccessRequestAuthorizationCreationQuery authorizationCreationQuery) {
        UUID financialInstitutionId = authorizationCreationQuery.getFinancialInstitutionId();
        UUID accountInformationAccessRequestId = authorizationCreationQuery.getAccountInformationAccessRequestId();

        URI uri = getUrl(financialInstitutionId, accountInformationAccessRequestId);

        AccountInformationAccessRequestAuthorization ibanityModel = mapAttributes(authorizationCreationQuery);
        AccountInformationAccessRequestAuthorizationMeta meta = mapMeta(authorizationCreationQuery);
        RequestApiModel request = buildRequest(AccountInformationAccessRequestAuthorization.RESOURCE_TYPE, ibanityModel, meta);

        HttpResponse response = ibanityHttpClient.post(uri, request, authorizationCreationQuery.getAdditionalHeaders(), authorizationCreationQuery.getCustomerAccessToken());
        return IbanityModelMapper.mapResource(response, com.ibanity.apis.client.products.xs2a.models.AccountInformationAccessRequestAuthorization.class);
    }

    private AccountInformationAccessRequestAuthorizationMeta mapMeta(AccountInformationAccessRequestAuthorizationCreationQuery authorizationCreationQuery) {
        return AccountInformationAccessRequestAuthorizationMeta.builder()
                .credentialsEncryptionKey(authorizationCreationQuery.getCredentialsEncryptionKey())
                .build();
    }

    private URI getUrl(UUID financialInstitutionId, UUID accountInformationAccessRequestId) {
        String url = apiUrlProvider.find(IbanityProduct.Xs2a, "customer", "financialInstitution", "accountInformationAccessRequest", "authorizations")
                .replace(FinancialInstitution.API_URL_TAG_ID, financialInstitutionId.toString())
                .replace(AccountInformationAccessRequest.API_URL_TAG_ID, accountInformationAccessRequestId.toString())
                .replace(com.ibanity.apis.client.products.xs2a.models.Authorization.API_URL_TAG_ID, "");

        return buildUri(StringUtils.removeEnd(url, "/"));
    }

    private AccountInformationAccessRequestAuthorization mapAttributes(AccountInformationAccessRequestAuthorizationCreationQuery authorizationCreationQuery) {
        return AccountInformationAccessRequestAuthorization.builder()
                .queryParameters(authorizationCreationQuery.getQueryParameters())
                .build();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    static class AccountInformationAccessRequestAuthorization implements IbanityModel {

        public static final String RESOURCE_TYPE = "authorization";
        public static final String API_URL_TAG_ID = "{" + RESOURCE_TYPE + URL_PARAMETER_ID_POSTFIX + "}";

        private UUID id;
        private String selfLink;
        private String requestId;

        @Builder.Default
        private Map<String, String> queryParameters = emptyMap();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    static class AccountInformationAccessRequestAuthorizationMeta {
        private String credentialsEncryptionKey;
    }
}
