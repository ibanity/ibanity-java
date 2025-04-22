package com.ibanity.apis.client.products.ponto_connect.services.impl;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.toIbanityModel;
import static com.ibanity.apis.client.mappers.IbanityModelMapper.mapResource;
import static com.ibanity.apis.client.mappers.ModelMapperHelper.buildRequest;
import static com.ibanity.apis.client.models.IbanityProduct.PontoConnect;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;

import java.net.URI;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.DataApiModel;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.products.ponto_connect.models.create.PaymentRequestActivationRequestCreateQuery;
import com.ibanity.apis.client.products.ponto_connect.services.PaymentRequestActivationRequestService;
import com.ibanity.apis.client.services.ApiUrlProvider;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class PaymentRequestActivationRequestServiceImpl implements PaymentRequestActivationRequestService {

    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public PaymentRequestActivationRequestServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public com.ibanity.apis.client.products.ponto_connect.models.PaymentRequestActivationRequest create(PaymentRequestActivationRequestCreateQuery paymentRequestActivationRequestCreateQuery) {
        URI uri = buildUri(getUrl());

        RequestApiModel requestApiModel = buildRequest(PaymentRequestActivationRequest.RESOURCE_TYPE, toRequest(paymentRequestActivationRequestCreateQuery));
        HttpResponse response = ibanityHttpClient.post(uri, requestApiModel, paymentRequestActivationRequestCreateQuery.getAdditionalHeaders(), paymentRequestActivationRequestCreateQuery.getAccessToken());

        return mapResource(response, customMapping());
    }

    private PaymentRequestActivationRequest toRequest(PaymentRequestActivationRequestCreateQuery paymentRequestActivationRequestCreateQuery) {
        return PaymentRequestActivationRequest.builder()
                .redirectUri(paymentRequestActivationRequestCreateQuery.getRedirectUri())
                .build();
    }

    private Function<DataApiModel, com.ibanity.apis.client.products.ponto_connect.models.PaymentRequestActivationRequest> customMapping() {
        return dataApiModel -> {
            com.ibanity.apis.client.products.ponto_connect.models.PaymentRequestActivationRequest paymentRequestActivationRequest = toIbanityModel(dataApiModel, com.ibanity.apis.client.products.ponto_connect.models.PaymentRequestActivationRequest.class);
            paymentRequestActivationRequest.setRedirectLink(dataApiModel.getLinks().getRedirect());

            return paymentRequestActivationRequest;
        };
    }

    private String getUrl() {
        return StringUtils.removeEnd(apiUrlProvider.find(PontoConnect, "paymentRequestActivationRequests"), "/");
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class PaymentRequestActivationRequest {
        public static final String RESOURCE_TYPE = "paymentRequestActivationRequest";

        private String redirectUri;
    }

}
