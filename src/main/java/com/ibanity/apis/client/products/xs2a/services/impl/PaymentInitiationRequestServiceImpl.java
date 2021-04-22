package com.ibanity.apis.client.products.xs2a.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.models.IbanityModel;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.xs2a.models.FinancialInstitution;
import com.ibanity.apis.client.products.xs2a.models.create.PaymentInitiationRequestCreationQuery;
import com.ibanity.apis.client.products.xs2a.models.read.PaymentInitiationRequestReadQuery;
import com.ibanity.apis.client.products.xs2a.services.PaymentInitiationRequestService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import lombok.*;
import org.apache.http.HttpResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.mapResource;
import static com.ibanity.apis.client.mappers.ModelMapperHelper.buildRequest;
import static com.ibanity.apis.client.products.xs2a.mappers.PaymentInitiationRequestMapper.getRequestMapping;
import static com.ibanity.apis.client.products.xs2a.mappers.PaymentInitiationRequestMapper.getResponseMapping;
import static com.ibanity.apis.client.products.xs2a.services.impl.PaymentInitiationRequestServiceImpl.PaymentInitiationRequest.RESOURCE_TYPE;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;
import static org.apache.commons.lang3.StringUtils.removeEnd;

public class PaymentInitiationRequestServiceImpl implements PaymentInitiationRequestService {

    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public PaymentInitiationRequestServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public com.ibanity.apis.client.products.xs2a.models.PaymentInitiationRequest create(PaymentInitiationRequestCreationQuery query) {
        PaymentInitiationRequest paymentInitiationRequest = getRequestMapping(query);
        RequestApiModel request = buildRequest(RESOURCE_TYPE, paymentInitiationRequest);

        String url = getUrl(query.getFinancialInstitutionId().toString(), "");
        HttpResponse response = ibanityHttpClient.post(buildUri(url), request, query.getAdditionalHeaders(), query.getCustomerAccessToken());

        return mapResource(response, getResponseMapping());
    }

    @Override
    public com.ibanity.apis.client.products.xs2a.models.PaymentInitiationRequest delete(PaymentInitiationRequestReadQuery paymentInitiationRequestReadQuery) {
        String financialInstitutionId = paymentInitiationRequestReadQuery.getFinancialInstitutionId().toString();
        String paymentInitiationRequestId = paymentInitiationRequestReadQuery.getPaymentInitiationRequestId().toString();

        String url = getUrl(financialInstitutionId, paymentInitiationRequestId);
        HttpResponse response = ibanityHttpClient.delete(buildUri(url), paymentInitiationRequestReadQuery.getAdditionalHeaders(), paymentInitiationRequestReadQuery.getCustomerAccessToken());

        return mapResource(response, getResponseMapping());
    }

    @Override
    public com.ibanity.apis.client.products.xs2a.models.PaymentInitiationRequest find(PaymentInitiationRequestReadQuery paymentInitiationRequestReadQuery) {
        String financialInstitutionId = paymentInitiationRequestReadQuery.getFinancialInstitutionId().toString();
        String paymentInitiationRequestId = paymentInitiationRequestReadQuery.getPaymentInitiationRequestId().toString();

        String url = getUrl(financialInstitutionId, paymentInitiationRequestId);
        HttpResponse response = ibanityHttpClient.get(buildUri(url), paymentInitiationRequestReadQuery.getAdditionalHeaders(), paymentInitiationRequestReadQuery.getCustomerAccessToken());

        return mapResource(response, getResponseMapping());
    }

    private String getUrl(String financialInstitutionId, String paymentInitiationRequestId) {
        return removeEnd(
                apiUrlProvider.find(IbanityProduct.Xs2a, "customer", "financialInstitution", "paymentInitiationRequests")
                        .replace(FinancialInstitution.API_URL_TAG_ID, financialInstitutionId)
                        .replace(PaymentInitiationRequest.API_URL_TAG_ID, paymentInitiationRequestId),
                "/");
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class PaymentInitiationRequest implements IbanityModel {

        public static final String RESOURCE_TYPE = "paymentInitiationRequest";
        public static final String API_URL_TAG_ID = "{" + RESOURCE_TYPE + URL_PARAMETER_ID_POSTFIX + "}";

        private UUID id;
        private UUID financialInstitutionId;
        private String selfLink;
        private String requestId;

        private String consentReference;
        private String endToEndId;
        private String productType;
        private String remittanceInformationType;
        private String remittanceInformation;
        private String currency;
        private String debtorName;
        private String debtorAccountReference;
        private String debtorAccountReferenceType;
        private String creditorName;
        private String creditorAccountReference;
        private String creditorAccountReferenceType;
        private String creditorAgent;
        private String creditorAgentType;
        private String status;
        private String redirectUri;
        private String locale;
        private String customerIpAddress;
        private boolean allowFinancialInstitutionRedirectUri;
        private boolean skipIbanityCompletionCallback;
        private String state;
        private String financialInstitutionCustomerReference;

        private BigDecimal amount;

        private LocalDate requestedExecutionDate;
    }
}
