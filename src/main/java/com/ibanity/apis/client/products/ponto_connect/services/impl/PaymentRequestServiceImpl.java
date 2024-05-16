package com.ibanity.apis.client.products.ponto_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.DataApiModel;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.ponto_connect.models.Account;
import com.ibanity.apis.client.products.ponto_connect.models.create.PaymentRequestCreateQuery;
import com.ibanity.apis.client.products.ponto_connect.models.delete.PaymentRequestDeleteQuery;
import com.ibanity.apis.client.products.ponto_connect.models.read.PaymentRequestReadQuery;
import com.ibanity.apis.client.products.ponto_connect.services.PaymentRequestService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import java.util.function.Function;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.mapResource;
import static com.ibanity.apis.client.mappers.IbanityModelMapper.toIbanityModel;
import static com.ibanity.apis.client.mappers.ModelMapperHelper.buildRequest;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;

public class PaymentRequestServiceImpl implements PaymentRequestService {

    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public PaymentRequestServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public com.ibanity.apis.client.products.ponto_connect.models.PaymentRequest find(PaymentRequestReadQuery paymentRequestReadQuery) {
        URI uri = buildUri(getUrl(paymentRequestReadQuery.getAccountId())
                + "/"
                + paymentRequestReadQuery.getPaymentRequestId().toString());

        HttpResponse response = ibanityHttpClient.get(uri, paymentRequestReadQuery.getAdditionalHeaders(), paymentRequestReadQuery.getAccessToken());
        return mapResource(response, com.ibanity.apis.client.products.ponto_connect.models.PaymentRequest.class);
    }

    @Override
    public com.ibanity.apis.client.products.ponto_connect.models.PaymentRequest create(PaymentRequestCreateQuery paymentRequestCreateQuery) {
        URI uri = buildUri(getUrl(paymentRequestCreateQuery.getAccountId()));

        PaymentRequest paymentRequest = toRequest(paymentRequestCreateQuery);
        RequestApiModel requestApiModel = buildRequest(com.ibanity.apis.client.products.ponto_connect.models.PaymentRequest.RESOURCE_TYPE, paymentRequest);
        HttpResponse response = ibanityHttpClient.post(uri, requestApiModel, paymentRequestCreateQuery.getAdditionalHeaders(), paymentRequestCreateQuery.getAccessToken());
        return mapResource(response, customMapping());
    }

    private Function<DataApiModel, com.ibanity.apis.client.products.ponto_connect.models.PaymentRequest> customMapping() {
        return dataApiModel -> {
            com.ibanity.apis.client.products.ponto_connect.models.PaymentRequest paymentRequest = toIbanityModel(dataApiModel, com.ibanity.apis.client.products.ponto_connect.models.PaymentRequest.class);
            if (dataApiModel.getLinks() != null) {
                paymentRequest.setRedirectLink(dataApiModel.getLinks().getRedirect());
            }

            return paymentRequest;
        };
    }

    @Override
    public com.ibanity.apis.client.products.ponto_connect.models.PaymentRequest delete(PaymentRequestDeleteQuery paymentRequestDeleteQuery) {
        URI uri = buildUri(getUrl(paymentRequestDeleteQuery.getAccountId())
                + "/"
                + paymentRequestDeleteQuery.getPaymentRequestId().toString());

        HttpResponse response = ibanityHttpClient.delete(uri, paymentRequestDeleteQuery.getAdditionalHeaders(), paymentRequestDeleteQuery.getAccessToken());
        return mapResource(response, com.ibanity.apis.client.products.ponto_connect.models.PaymentRequest.class);
    }

    private String getUrl(UUID accountId) {
        return StringUtils.removeEnd(
                apiUrlProvider.find(IbanityProduct.PontoConnect, "account", "paymentRequests")
                        .replace(Account.API_URL_TAG_ID, accountId.toString())
                        .replace(com.ibanity.apis.client.products.ponto_connect.models.PaymentRequest.API_URL_TAG_ID, ""),
                "/");
    }

    private PaymentRequest toRequest(PaymentRequestCreateQuery paymentRequestCreateQuery) {
        return PaymentRequest.builder()
                .amount(paymentRequestCreateQuery.getAmount())
                .remittanceInformation(paymentRequestCreateQuery.getRemittanceInformation())
                .remittanceInformationType(paymentRequestCreateQuery.getRemittanceInformationType())
                .redirectUri(paymentRequestCreateQuery.getRedirectUri())
                .endToEndId(paymentRequestCreateQuery.getEndToEndId())
                .build();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class PaymentRequest {
        private String remittanceInformationType;
        private String remittanceInformation;
        private String currency;
        private BigDecimal amount;
        private String creditorAccountReferenceType;
        private String creditorAccountReference;
        private String debtorAccountReferenceType;
        private String debtorAccountReference;
        private String redirectUri;
        private String endToEndId;
        private String signingUri;
        private Instant closedAt;
        private Instant signedAt;
    }
}
