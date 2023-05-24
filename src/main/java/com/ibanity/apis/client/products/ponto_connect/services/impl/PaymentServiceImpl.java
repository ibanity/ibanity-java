package com.ibanity.apis.client.products.ponto_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.DataApiModel;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.ponto_connect.models.Account;
import com.ibanity.apis.client.products.ponto_connect.models.create.PaymentCreateQuery;
import com.ibanity.apis.client.products.ponto_connect.models.delete.PaymentDeleteQuery;
import com.ibanity.apis.client.products.ponto_connect.models.read.PaymentReadQuery;
import com.ibanity.apis.client.products.ponto_connect.services.PaymentService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.UUID;
import java.util.function.Function;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.mapResource;
import static com.ibanity.apis.client.mappers.IbanityModelMapper.toIbanityModel;
import static com.ibanity.apis.client.mappers.ModelMapperHelper.buildRequest;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;

public class PaymentServiceImpl implements PaymentService {

    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public PaymentServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public com.ibanity.apis.client.products.ponto_connect.models.Payment find(PaymentReadQuery paymentReadQuery) {
        URI uri = buildUri(getUrl(paymentReadQuery.getAccountId())
                + "/"
                + paymentReadQuery.getPaymentId().toString());

        HttpResponse response = ibanityHttpClient.get(uri, paymentReadQuery.getAdditionalHeaders(), paymentReadQuery.getAccessToken());
        return mapResource(response, com.ibanity.apis.client.products.ponto_connect.models.Payment.class);
    }

    @Override
    public com.ibanity.apis.client.products.ponto_connect.models.Payment create(PaymentCreateQuery paymentCreateQuery) {
        URI uri = buildUri(getUrl(paymentCreateQuery.getAccountId()));

        Payment payment = toRequest(paymentCreateQuery);
        RequestApiModel requestApiModel = buildRequest(com.ibanity.apis.client.products.ponto_connect.models.Payment.RESOURCE_TYPE, payment);
        HttpResponse response = ibanityHttpClient.post(uri, requestApiModel, paymentCreateQuery.getAdditionalHeaders(), paymentCreateQuery.getAccessToken());
        return mapResource(response, customMapping());
    }

    private Function<DataApiModel, com.ibanity.apis.client.products.ponto_connect.models.Payment> customMapping() {
        return dataApiModel -> {
            com.ibanity.apis.client.products.ponto_connect.models.Payment payment = toIbanityModel(dataApiModel, com.ibanity.apis.client.products.ponto_connect.models.Payment.class);
            if (dataApiModel.getLinks() != null) {
                payment.setRedirectLink(dataApiModel.getLinks().getRedirect());
            }

            return payment;
        };
    }

    @Override
    public com.ibanity.apis.client.products.ponto_connect.models.Payment delete(PaymentDeleteQuery paymentDeleteQuery) {
        URI uri = buildUri(getUrl(paymentDeleteQuery.getAccountId())
                + "/"
                + paymentDeleteQuery.getPaymentId().toString());

        HttpResponse response = ibanityHttpClient.delete(uri, paymentDeleteQuery.getAdditionalHeaders(), paymentDeleteQuery.getAccessToken());
        return mapResource(response, com.ibanity.apis.client.products.ponto_connect.models.Payment.class);
    }

    private String getUrl(UUID accountId) {
        return StringUtils.removeEnd(
                apiUrlProvider.find(IbanityProduct.PontoConnect, "account", "payments")
                        .replace(Account.API_URL_TAG_ID, accountId.toString())
                        .replace(com.ibanity.apis.client.products.ponto_connect.models.Payment.API_URL_TAG_ID, ""),
                "/");
    }

    private Payment toRequest(PaymentCreateQuery paymentCreateQuery) {
        return Payment.builder()
                .amount(paymentCreateQuery.getAmount())
                .creditorAccountReference(paymentCreateQuery.getCreditorAccountReference())
                .creditorAccountReferenceType(paymentCreateQuery.getCreditorAccountReferenceType())
                .creditorAgent(paymentCreateQuery.getCreditorAgent())
                .creditorAgentType(paymentCreateQuery.getCreditorAgentType())
                .creditorName(paymentCreateQuery.getCreditorName())
                .currency(paymentCreateQuery.getCurrency())
                .remittanceInformation(paymentCreateQuery.getRemittanceInformation())
                .remittanceInformationType(paymentCreateQuery.getRemittanceInformationType())
                .requestedExecutionDate(paymentCreateQuery.getRequestedExecutionDate())
                .redirectUri(paymentCreateQuery.getRedirectUri())
                .endToEndId(paymentCreateQuery.getEndToEndId())
                .build();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class Payment {
        private LocalDate requestedExecutionDate;
        private String remittanceInformationType;
        private String remittanceInformation;
        private String currency;
        private BigDecimal amount;
        private String creditorName;
        private String creditorAgentType;
        private String creditorAgent;
        private String creditorAccountReferenceType;
        private String creditorAccountReference;
        private String redirectUri;
        private String endToEndId;
    }
}
