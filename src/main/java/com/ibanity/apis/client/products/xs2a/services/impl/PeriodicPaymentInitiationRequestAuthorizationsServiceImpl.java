package com.ibanity.apis.client.products.xs2a.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.DataApiModel;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.mappers.IbanityModelMapper;
import com.ibanity.apis.client.models.IbanityModel;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.xs2a.models.FinancialInstitution;
import com.ibanity.apis.client.products.xs2a.models.PeriodicPaymentInitiationRequest;
import com.ibanity.apis.client.products.xs2a.models.create.PeriodicPaymentInitiationRequestAuthorizationCreationQuery;
import com.ibanity.apis.client.products.xs2a.models.links.PeriodicPaymentInitiationRequestAuthorizationLinks;
import com.ibanity.apis.client.products.xs2a.services.PeriodicPaymentInitiationRequestAuthorizationsService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;

import java.net.URI;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.toIbanityModel;
import static com.ibanity.apis.client.mappers.ModelMapperHelper.buildRequest;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;
import static java.util.Collections.emptyMap;

public class PeriodicPaymentInitiationRequestAuthorizationsServiceImpl implements PeriodicPaymentInitiationRequestAuthorizationsService {

    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public PeriodicPaymentInitiationRequestAuthorizationsServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public com.ibanity.apis.client.products.xs2a.models.PeriodicPaymentInitiationRequestAuthorization create(PeriodicPaymentInitiationRequestAuthorizationCreationQuery authorizationCreationQuery) {
        UUID financialInstitutionId = authorizationCreationQuery.getFinancialInstitutionId();
        UUID paymentInitiationRequestId = authorizationCreationQuery.getPaymentInitiationRequestId();

        URI uri = getUrl(financialInstitutionId, paymentInitiationRequestId);

        PeriodicPaymentInitiationRequestAuthorization ibanityModel = mapAttributes(authorizationCreationQuery);
        RequestApiModel request = buildRequest(PeriodicPaymentInitiationRequestAuthorization.RESOURCE_TYPE, ibanityModel);

        HttpResponse response = ibanityHttpClient.post(uri, request, authorizationCreationQuery.getAdditionalHeaders(), authorizationCreationQuery.getCustomerAccessToken());
        return IbanityModelMapper.mapResource(response, responseMapping());
    }

    private Function<DataApiModel, com.ibanity.apis.client.products.xs2a.models.PeriodicPaymentInitiationRequestAuthorization> responseMapping() {
        return dataApiModel -> {
            com.ibanity.apis.client.products.xs2a.models.PeriodicPaymentInitiationRequestAuthorization periodicPaymentInitiationRequestAuthorization = toIbanityModel(dataApiModel, com.ibanity.apis.client.products.xs2a.models.PeriodicPaymentInitiationRequestAuthorization.class);
            if (dataApiModel.getLinks() != null && dataApiModel.getLinks().getNextRedirect() != null) {
                periodicPaymentInitiationRequestAuthorization.setLinks(PeriodicPaymentInitiationRequestAuthorizationLinks.builder()
                        .nextRedirect(dataApiModel.getLinks().getNextRedirect())
                        .build());
            }

            return periodicPaymentInitiationRequestAuthorization;
        };
    }

    private URI getUrl(UUID financialInstitutionId, UUID paymentInitiationRequestId) {
        String url = apiUrlProvider.find(IbanityProduct.Xs2a, "customer", "financialInstitution", "periodicPaymentInitiationRequest", "authorizations")
                .replace(FinancialInstitution.API_URL_TAG_ID, financialInstitutionId.toString())
                .replace(PeriodicPaymentInitiationRequest.API_URL_TAG_ID, paymentInitiationRequestId.toString())
                .replace(PeriodicPaymentInitiationRequestAuthorization.API_URL_TAG_ID, "");

        return buildUri(StringUtils.removeEnd(url, "/"));
    }

    private PeriodicPaymentInitiationRequestAuthorization mapAttributes(PeriodicPaymentInitiationRequestAuthorizationCreationQuery authorizationCreationQuery) {
        return PeriodicPaymentInitiationRequestAuthorization.builder()
                .queryParameters(authorizationCreationQuery.getQueryParameters())
                .build();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    static class PeriodicPaymentInitiationRequestAuthorization implements IbanityModel {

        public static final String RESOURCE_TYPE = "authorization";
        public static final String API_URL_TAG_ID = "{" + RESOURCE_TYPE + URL_PARAMETER_ID_POSTFIX + "}";

        private UUID id;
        private String selfLink;
        private String requestId;

        @Builder.Default
        private Map<String, String> queryParameters = emptyMap();
    }
}
