package com.ibanity.apis.client.products.ponto_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.DataApiModel;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.models.IbanityModel;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.ponto_connect.models.Account;
import com.ibanity.apis.client.products.ponto_connect.models.create.BulkPaymentCreateQuery;
import com.ibanity.apis.client.products.ponto_connect.models.delete.BulkPaymentDeleteQuery;
import com.ibanity.apis.client.products.ponto_connect.models.read.BulkPaymentReadQuery;
import com.ibanity.apis.client.products.ponto_connect.services.BulkPaymentService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.mapResource;
import static com.ibanity.apis.client.mappers.IbanityModelMapper.toIbanityModel;
import static com.ibanity.apis.client.mappers.ModelMapperHelper.buildRequest;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;
import static java.util.stream.Collectors.toList;

public class BulkPaymentServiceImpl implements BulkPaymentService {

    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public BulkPaymentServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public com.ibanity.apis.client.products.ponto_connect.models.BulkPayment find(BulkPaymentReadQuery bulkPaymentReadQuery) {
        URI uri = buildUri(getUrl(bulkPaymentReadQuery.getAccountId().toString(), bulkPaymentReadQuery.getBulkPaymentId().toString()));

        HttpResponse response = ibanityHttpClient.get(uri, bulkPaymentReadQuery.getAdditionalHeaders(), bulkPaymentReadQuery.getAccessToken());
        return mapResource(response, com.ibanity.apis.client.products.ponto_connect.models.BulkPayment.class);
    }

    @Override
    public com.ibanity.apis.client.products.ponto_connect.models.BulkPayment create(BulkPaymentCreateQuery bulkPaymentCreateQuery) {
        URI uri = buildUri(getUrl(bulkPaymentCreateQuery.getAccountId().toString(), ""));

        BulkPayment bulkPayment = toRequest(bulkPaymentCreateQuery);
        RequestApiModel requestApiModel = buildRequest(BulkPayment.RESOURCE_TYPE, bulkPayment);
        HttpResponse response = ibanityHttpClient.post(uri, requestApiModel, bulkPaymentCreateQuery.getAdditionalHeaders(), bulkPaymentCreateQuery.getAccessToken());
        return mapResource(response, customMapping());
    }

    private Function<DataApiModel, com.ibanity.apis.client.products.ponto_connect.models.BulkPayment> customMapping() {
        return dataApiModel -> {
            com.ibanity.apis.client.products.ponto_connect.models.BulkPayment payment = toIbanityModel(dataApiModel, com.ibanity.apis.client.products.ponto_connect.models.BulkPayment.class);
            if (dataApiModel.getLinks() != null) {
                payment.setRedirectLink(dataApiModel.getLinks().getRedirect());
            }

            return payment;
        };
    }

    @Override
    public com.ibanity.apis.client.products.ponto_connect.models.BulkPayment delete(BulkPaymentDeleteQuery bulkPaymentDeleteQuery) {
        URI uri = buildUri(getUrl(bulkPaymentDeleteQuery.getAccountId().toString(), bulkPaymentDeleteQuery.getBulkPaymentId().toString()));

        HttpResponse response = ibanityHttpClient.delete(uri, bulkPaymentDeleteQuery.getAdditionalHeaders(), bulkPaymentDeleteQuery.getAccessToken());
        return mapResource(response, com.ibanity.apis.client.products.ponto_connect.models.BulkPayment.class);
    }

    private String getUrl(String accountId, String bulkPaymentId) {
        return StringUtils.removeEnd(
                apiUrlProvider.find(IbanityProduct.PontoConnect, "account", "bulkPayments")
                        .replace(Account.API_URL_TAG_ID, accountId)
                        .replace(BulkPayment.API_URL_TAG_ID, bulkPaymentId),
                "/");
    }

    private BulkPayment toRequest(BulkPaymentCreateQuery bulkPaymentCreateQuery) {
        return BulkPayment.builder()
                .requestedExecutionDate(bulkPaymentCreateQuery.getRequestedExecutionDate())
                .reference(bulkPaymentCreateQuery.getReference())
                .redirectUri(bulkPaymentCreateQuery.getRedirectUri())
                .batchBookingPreferred(bulkPaymentCreateQuery.isBatchBookingPreferred())
                .payments(
                        bulkPaymentCreateQuery.getPayments().stream()
                                .map(this::toPayment)
                                .collect(toList())
                )
                .build();
    }

    private BulkPayment.Payment toPayment(BulkPaymentCreateQuery.Payment payment) {
        return BulkPayment.Payment.builder()
                .amount(payment.getAmount())
                .creditorAccountReference(payment.getCreditorAccountReference())
                .creditorAccountReferenceType(payment.getCreditorAccountReferenceType())
                .creditorAgent(payment.getCreditorAgent())
                .creditorAgentType(payment.getCreditorAgentType())
                .creditorName(payment.getCreditorName())
                .currency(payment.getCurrency())
                .remittanceInformation(payment.getRemittanceInformation())
                .remittanceInformationType(payment.getRemittanceInformationType())
                .build();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class BulkPayment implements IbanityModel {

        public static final String RESOURCE_TYPE = "bulkPayment";
        public static final String API_URL_TAG_ID = "{" + RESOURCE_TYPE + URL_PARAMETER_ID_POSTFIX + "}";

        private UUID id;
        private String selfLink;
        private String requestId;

        private String status;
        private LocalDate requestedExecutionDate;
        private String reference;
        private String redirectUri;
        private boolean batchBookingPreferred;


        @Builder.Default
        private List<BulkPayment.Payment> payments = Collections.emptyList();

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor(access = AccessLevel.PRIVATE)
        private static class Payment {

            private String remittanceInformationType;
            private String remittanceInformation;
            private String currency;
            private String creditorName;
            private String creditorAgentType;
            private String creditorAgent;
            private String creditorAccountReferenceType;
            private String creditorAccountReference;
            private BigDecimal amount;
        }
    }

}
