package com.ibanity.apis.client.products.ponto_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.DataApiModel;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.products.ponto_connect.models.create.PaymentActivationRequestCreateQuery;
import com.ibanity.apis.client.products.ponto_connect.services.PaymentActivationRequestService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;

import java.net.URI;
import java.util.function.Function;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.mapResource;
import static com.ibanity.apis.client.mappers.IbanityModelMapper.toIbanityModel;
import static com.ibanity.apis.client.mappers.ModelMapperHelper.buildRequest;
import static com.ibanity.apis.client.models.IbanityProduct.PontoConnect;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;

public class PaymentActivationRequestServiceImpl implements PaymentActivationRequestService {

    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public PaymentActivationRequestServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public com.ibanity.apis.client.products.ponto_connect.models.PaymentActivationRequest create(PaymentActivationRequestCreateQuery paymentActivationRequestCreateQuery) {
        URI uri = buildUri(getUrl());

        RequestApiModel requestApiModel = buildRequest(PaymentActivationRequest.RESOURCE_TYPE, toRequest(paymentActivationRequestCreateQuery));
        HttpResponse response = ibanityHttpClient.post(uri, requestApiModel, paymentActivationRequestCreateQuery.getAdditionalHeaders(), paymentActivationRequestCreateQuery.getAccessToken());

        return mapResource(response, customMapping());
    }

    private PaymentActivationRequest toRequest(PaymentActivationRequestCreateQuery paymentActivationRequestCreateQuery) {
        PaymentActivationRequest.builder()
                .redirectUri(paymentActivationRequestCreateQuery.getRedirectUri())
                .build();
        return null;
    }

    private Function<DataApiModel, com.ibanity.apis.client.products.ponto_connect.models.PaymentActivationRequest> customMapping() {
        return dataApiModel -> {
            com.ibanity.apis.client.products.ponto_connect.models.PaymentActivationRequest paymentActivationRequest = toIbanityModel(dataApiModel, com.ibanity.apis.client.products.ponto_connect.models.PaymentActivationRequest.class);
            paymentActivationRequest.setRedirectLink(dataApiModel.getLinks().getRedirect());

            return paymentActivationRequest;
        };
    }

    private String getUrl() {
        return StringUtils.removeEnd(apiUrlProvider.find(PontoConnect, "paymentAuthorizationRequests"), "/");
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class PaymentActivationRequest {
        public static final String RESOURCE_TYPE = "paymentActivationRequest";

        private String redirectUri;
    }
}
