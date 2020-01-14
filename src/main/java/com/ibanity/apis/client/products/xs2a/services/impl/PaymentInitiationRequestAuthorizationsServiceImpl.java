package com.ibanity.apis.client.products.xs2a.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.DataApiModel;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.mappers.IbanityModelMapper;
import com.ibanity.apis.client.models.IbanityModel;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.xs2a.models.FinancialInstitution;
import com.ibanity.apis.client.products.xs2a.models.PaymentInitiationRequest;
import com.ibanity.apis.client.products.xs2a.models.create.PaymentInitiationRequestAuthorizationCreationQuery;
import com.ibanity.apis.client.products.xs2a.models.links.PaymentInitiationRequestAuthorizationLinks;
import com.ibanity.apis.client.products.xs2a.services.PaymentInitiationRequestAuthorizationsService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.buildRequest;
import static com.ibanity.apis.client.mappers.IbanityModelMapper.toIbanityModel;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;
import static java.util.Collections.emptyMap;

public class PaymentInitiationRequestAuthorizationsServiceImpl implements PaymentInitiationRequestAuthorizationsService {

    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public PaymentInitiationRequestAuthorizationsServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public com.ibanity.apis.client.products.xs2a.models.PaymentInitiationRequestAuthorization create(PaymentInitiationRequestAuthorizationCreationQuery authorizationCreationQuery) {
        UUID financialInstitutionId = authorizationCreationQuery.getFinancialInstitutionId();
        UUID paymentInitiationRequestId = authorizationCreationQuery.getPaymentInitiationRequestId();

        URI uri = getUrl(financialInstitutionId, paymentInitiationRequestId);

        PaymentInitiationRequestAuthorization ibanityModel = mapAttributes(authorizationCreationQuery);
        RequestApiModel request = buildRequest(PaymentInitiationRequestAuthorization.RESOURCE_TYPE, ibanityModel);

        String response = ibanityHttpClient.post(uri, request, authorizationCreationQuery.getAdditionalHeaders(), authorizationCreationQuery.getCustomerAccessToken());
        return IbanityModelMapper.mapResource(response, responseMapping());
    }

    private Function<DataApiModel, com.ibanity.apis.client.products.xs2a.models.PaymentInitiationRequestAuthorization> responseMapping() {
        return dataApiModel -> {
            com.ibanity.apis.client.products.xs2a.models.PaymentInitiationRequestAuthorization paymentInitiationRequestAuthorization = toIbanityModel(dataApiModel, com.ibanity.apis.client.products.xs2a.models.PaymentInitiationRequestAuthorization.class);
            if (dataApiModel.getLinks() != null && dataApiModel.getLinks().getNextRedirect() != null) {
                paymentInitiationRequestAuthorization.setLinks(PaymentInitiationRequestAuthorizationLinks.builder()
                        .nextRedirect(dataApiModel.getLinks().getNextRedirect())
                        .build());
            }

            return paymentInitiationRequestAuthorization;
        };
    }

    private URI getUrl(UUID financialInstitutionId, UUID paymentInitiationRequestId) {
        String url = apiUrlProvider.find(IbanityProduct.Xs2a, "customer", "financialInstitution", "paymentInitiationRequest", "authorizations")
                .replace(FinancialInstitution.API_URL_TAG_ID, financialInstitutionId.toString())
                .replace(PaymentInitiationRequest.API_URL_TAG_ID, paymentInitiationRequestId.toString())
                .replace(PaymentInitiationRequestAuthorization.API_URL_TAG_ID, "");

        return buildUri(StringUtils.removeEnd(url, "/"));
    }

    private PaymentInitiationRequestAuthorization mapAttributes(PaymentInitiationRequestAuthorizationCreationQuery authorizationCreationQuery) {
        return PaymentInitiationRequestAuthorization.builder()
                .queryParameters(authorizationCreationQuery.getQueryParameters())
                .build();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    static class PaymentInitiationRequestAuthorization implements IbanityModel {

        public static final String RESOURCE_TYPE = "authorization";
        public static final String API_URL_TAG_ID = "{" + RESOURCE_TYPE + URL_PARAMETER_ID_POSTFIX + "}";

        private UUID id;
        private String selfLink;

        @Builder.Default
        private Map<String, String> queryParameters = emptyMap();
    }
}
