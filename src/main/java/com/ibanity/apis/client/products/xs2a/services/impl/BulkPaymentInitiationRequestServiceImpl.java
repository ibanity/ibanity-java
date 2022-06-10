package com.ibanity.apis.client.products.xs2a.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.models.IbanityModel;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.xs2a.models.FinancialInstitution;
import com.ibanity.apis.client.products.xs2a.models.create.BulkPaymentInitiationRequestCreationQuery;
import com.ibanity.apis.client.products.xs2a.models.read.BulkPaymentInitiationRequestReadQuery;
import com.ibanity.apis.client.products.xs2a.services.BulkPaymentInitiationRequestService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.http.HttpResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.mapResource;
import static com.ibanity.apis.client.mappers.ModelMapperHelper.buildRequest;
import static com.ibanity.apis.client.products.xs2a.mappers.BulkPaymentInitiationRequestMapper.getRequestMapping;
import static com.ibanity.apis.client.products.xs2a.mappers.BulkPaymentInitiationRequestMapper.getResponseMapping;
import static com.ibanity.apis.client.products.xs2a.services.impl.BulkPaymentInitiationRequestServiceImpl.BulkPaymentInitiationRequest.RESOURCE_TYPE;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;
import static org.apache.commons.lang3.StringUtils.removeEnd;

public class BulkPaymentInitiationRequestServiceImpl implements BulkPaymentInitiationRequestService {

    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public BulkPaymentInitiationRequestServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public com.ibanity.apis.client.products.xs2a.models.BulkPaymentInitiationRequest create(BulkPaymentInitiationRequestCreationQuery query) {
        BulkPaymentInitiationRequest bulkPaymentInitiationRequest = getRequestMapping(query);
        RequestApiModel request = buildRequest(RESOURCE_TYPE, bulkPaymentInitiationRequest);

        String url = getUrl(query.getFinancialInstitutionId().toString(), "");
        HttpResponse response = ibanityHttpClient.post(buildUri(url), request, query.getAdditionalHeaders(), query.getCustomerAccessToken());

        return mapResource(response, getResponseMapping());
    }

    @Override
    public com.ibanity.apis.client.products.xs2a.models.BulkPaymentInitiationRequest delete(BulkPaymentInitiationRequestReadQuery bulkPaymentInitiationRequestReadQuery) {
        String financialInstitutionId = bulkPaymentInitiationRequestReadQuery.getFinancialInstitutionId().toString();
        String paymentInitiationRequestId = bulkPaymentInitiationRequestReadQuery.getPaymentInitiationRequestId().toString();

        String url = getUrl(financialInstitutionId, paymentInitiationRequestId);
        HttpResponse response = ibanityHttpClient.delete(buildUri(url), bulkPaymentInitiationRequestReadQuery.getAdditionalHeaders(), bulkPaymentInitiationRequestReadQuery.getCustomerAccessToken());

        return mapResource(response, getResponseMapping());
    }

    @Override
    public com.ibanity.apis.client.products.xs2a.models.BulkPaymentInitiationRequest find(BulkPaymentInitiationRequestReadQuery bulkPaymentInitiationRequestReadQuery) {
        String financialInstitutionId = bulkPaymentInitiationRequestReadQuery.getFinancialInstitutionId().toString();
        String paymentInitiationRequestId = bulkPaymentInitiationRequestReadQuery.getPaymentInitiationRequestId().toString();

        String url = getUrl(financialInstitutionId, paymentInitiationRequestId);
        HttpResponse response = ibanityHttpClient.get(buildUri(url), bulkPaymentInitiationRequestReadQuery.getAdditionalHeaders(), bulkPaymentInitiationRequestReadQuery.getCustomerAccessToken());

        return mapResource(response, getResponseMapping());
    }

    private String getUrl(String financialInstitutionId, String paymentInitiationRequestId) {
        return removeEnd(
                apiUrlProvider.find(IbanityProduct.Xs2a, "customer", "financialInstitution", "bulkPaymentInitiationRequests")
                        .replace(FinancialInstitution.API_URL_TAG_ID, financialInstitutionId)
                        .replace(BulkPaymentInitiationRequest.API_URL_TAG_ID, paymentInitiationRequestId),
                "/");
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class BulkPaymentInitiationRequest implements IbanityModel {

        public static final String RESOURCE_TYPE = "bulkPaymentInitiationRequest";
        public static final String API_URL_TAG_ID = "{paymentInitiationRequest" + URL_PARAMETER_ID_POSTFIX + "}";

        private UUID id;
        private UUID financialInstitutionId;
        private String selfLink;
        private String requestId;

        private String consentReference;
        private String productType;
        private String debtorName;
        private String debtorAccountReference;
        private String debtorAccountReferenceType;
        private String status;
        private String statusReason;
        private String redirectUri;
        private String locale;
        private String customerIpAddress;
        private boolean allowFinancialInstitutionRedirectUri;
        private boolean skipIbanityCompletionCallback;
        private boolean batchBookingPreferred;
        private String state;
        private String financialInstitutionCustomerReference;

        private LocalDate requestedExecutionDate;

        @Builder.Default
        private List<Payment> payments = Collections.emptyList();

        @Getter
        @Builder
        @ToString
        @EqualsAndHashCode
        public static class Payment {
            private String endToEndId;

            private String remittanceInformationType;
            private String remittanceInformation;

            private BigDecimal amount;
            private String currency;

            private String creditorName;
            private String creditorAccountReference;
            private String creditorAccountReferenceType;
            private String creditorAgent;
            private String creditorAgentType;
        }
    }
}
