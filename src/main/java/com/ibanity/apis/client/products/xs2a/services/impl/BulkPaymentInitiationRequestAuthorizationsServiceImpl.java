package com.ibanity.apis.client.products.xs2a.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.DataApiModel;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.mappers.IbanityModelMapper;
import com.ibanity.apis.client.models.IbanityModel;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.xs2a.models.BulkPaymentInitiationRequest;
import com.ibanity.apis.client.products.xs2a.models.FinancialInstitution;
import com.ibanity.apis.client.products.xs2a.models.create.BulkPaymentInitiationRequestAuthorizationCreationQuery;
import com.ibanity.apis.client.products.xs2a.models.links.BulkPaymentInitiationRequestAuthorizationLinks;
import com.ibanity.apis.client.products.xs2a.services.BulkPaymentInitiationRequestAuthorizationsService;
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

public class BulkPaymentInitiationRequestAuthorizationsServiceImpl implements BulkPaymentInitiationRequestAuthorizationsService {

    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public BulkPaymentInitiationRequestAuthorizationsServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public com.ibanity.apis.client.products.xs2a.models.BulkPaymentInitiationRequestAuthorization create(BulkPaymentInitiationRequestAuthorizationCreationQuery authorizationCreationQuery) {
        UUID financialInstitutionId = authorizationCreationQuery.getFinancialInstitutionId();
        UUID paymentInitiationRequestId = authorizationCreationQuery.getPaymentInitiationRequestId();

        URI uri = getUrl(financialInstitutionId, paymentInitiationRequestId);

        BulkPaymentInitiationRequestAuthorization ibanityModel = mapAttributes(authorizationCreationQuery);
        RequestApiModel request = buildRequest(BulkPaymentInitiationRequestAuthorization.RESOURCE_TYPE, ibanityModel);

        HttpResponse response = ibanityHttpClient.post(uri, request, authorizationCreationQuery.getAdditionalHeaders(), authorizationCreationQuery.getCustomerAccessToken());
        return IbanityModelMapper.mapResource(response, responseMapping());
    }

    private Function<DataApiModel, com.ibanity.apis.client.products.xs2a.models.BulkPaymentInitiationRequestAuthorization> responseMapping() {
        return dataApiModel -> {
            com.ibanity.apis.client.products.xs2a.models.BulkPaymentInitiationRequestAuthorization bulkPaymentInitiationRequestAuthorization = toIbanityModel(dataApiModel, com.ibanity.apis.client.products.xs2a.models.BulkPaymentInitiationRequestAuthorization.class);
            if (dataApiModel.getLinks() != null && dataApiModel.getLinks().getNextRedirect() != null) {
                bulkPaymentInitiationRequestAuthorization.setLinks(BulkPaymentInitiationRequestAuthorizationLinks.builder()
                        .nextRedirect(dataApiModel.getLinks().getNextRedirect())
                        .build());
            }

            return bulkPaymentInitiationRequestAuthorization;
        };
    }

    private URI getUrl(UUID financialInstitutionId, UUID paymentInitiationRequestId) {
        String url = apiUrlProvider.find(IbanityProduct.Xs2a, "customer", "financialInstitution", "bulkPaymentInitiationRequest", "authorizations")
                .replace(FinancialInstitution.API_URL_TAG_ID, financialInstitutionId.toString())
                .replace(BulkPaymentInitiationRequest.API_URL_TAG_ID, paymentInitiationRequestId.toString())
                .replace(BulkPaymentInitiationRequestAuthorization.API_URL_TAG_ID, "");

        return buildUri(StringUtils.removeEnd(url, "/"));
    }

    private BulkPaymentInitiationRequestAuthorization mapAttributes(BulkPaymentInitiationRequestAuthorizationCreationQuery authorizationCreationQuery) {
        return BulkPaymentInitiationRequestAuthorization.builder()
                .queryParameters(authorizationCreationQuery.getQueryParameters())
                .build();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    static class BulkPaymentInitiationRequestAuthorization implements IbanityModel {

        public static final String RESOURCE_TYPE = "authorization";
        public static final String API_URL_TAG_ID = "{" + RESOURCE_TYPE + URL_PARAMETER_ID_POSTFIX + "}";

        private UUID id;
        private String selfLink;
        private String requestId;

        @Builder.Default
        private Map<String, String> queryParameters = emptyMap();
    }
}
