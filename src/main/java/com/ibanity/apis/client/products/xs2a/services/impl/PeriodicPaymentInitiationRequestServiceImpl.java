package com.ibanity.apis.client.products.xs2a.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.models.IbanityModel;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.xs2a.models.FinancialInstitution;
import com.ibanity.apis.client.products.xs2a.models.create.PeriodicPaymentInitiationRequestCreationQuery;
import com.ibanity.apis.client.products.xs2a.models.read.PeriodicPaymentInitiationRequestReadQuery;
import com.ibanity.apis.client.products.xs2a.services.PeriodicPaymentInitiationRequestService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.http.HttpResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.mapResource;
import static com.ibanity.apis.client.mappers.ModelMapperHelper.buildRequest;
import static com.ibanity.apis.client.products.xs2a.mappers.PeriodicPaymentInitiationRequestMapper.getRequestMapping;
import static com.ibanity.apis.client.products.xs2a.mappers.PeriodicPaymentInitiationRequestMapper.getResponseMapping;
import static com.ibanity.apis.client.products.xs2a.services.impl.PeriodicPaymentInitiationRequestServiceImpl.PeriodicPaymentInitiationRequest.RESOURCE_TYPE;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;
import static org.apache.commons.lang3.StringUtils.removeEnd;

public class PeriodicPaymentInitiationRequestServiceImpl implements PeriodicPaymentInitiationRequestService {

    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public PeriodicPaymentInitiationRequestServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public com.ibanity.apis.client.products.xs2a.models.PeriodicPaymentInitiationRequest create(PeriodicPaymentInitiationRequestCreationQuery query) {
        PeriodicPaymentInitiationRequest periodicPaymentInitiationRequest = getRequestMapping(query);
        RequestApiModel request = buildRequest(RESOURCE_TYPE, periodicPaymentInitiationRequest);

        String url = getUrl(query.getFinancialInstitutionId().toString(), "");
        HttpResponse response = ibanityHttpClient.post(buildUri(url), request, query.getAdditionalHeaders(), query.getCustomerAccessToken());

        return mapResource(response, getResponseMapping());
    }

    @Override
    public com.ibanity.apis.client.products.xs2a.models.PeriodicPaymentInitiationRequest delete(PeriodicPaymentInitiationRequestReadQuery periodicPaymentInitiationRequestReadQuery) {
        String financialInstitutionId = periodicPaymentInitiationRequestReadQuery.getFinancialInstitutionId().toString();
        String paymentInitiationRequestId = periodicPaymentInitiationRequestReadQuery.getPaymentInitiationRequestId().toString();

        String url = getUrl(financialInstitutionId, paymentInitiationRequestId);
        HttpResponse response = ibanityHttpClient.delete(buildUri(url), periodicPaymentInitiationRequestReadQuery.getAdditionalHeaders(), periodicPaymentInitiationRequestReadQuery.getCustomerAccessToken());

        return mapResource(response, getResponseMapping());
    }

    @Override
    public com.ibanity.apis.client.products.xs2a.models.PeriodicPaymentInitiationRequest find(PeriodicPaymentInitiationRequestReadQuery periodicPaymentInitiationRequestReadQuery) {
        String financialInstitutionId = periodicPaymentInitiationRequestReadQuery.getFinancialInstitutionId().toString();
        String paymentInitiationRequestId = periodicPaymentInitiationRequestReadQuery.getPaymentInitiationRequestId().toString();

        String url = getUrl(financialInstitutionId, paymentInitiationRequestId);
        HttpResponse response = ibanityHttpClient.get(buildUri(url), periodicPaymentInitiationRequestReadQuery.getAdditionalHeaders(), periodicPaymentInitiationRequestReadQuery.getCustomerAccessToken());

        return mapResource(response, getResponseMapping());
    }

    private String getUrl(String financialInstitutionId, String paymentInitiationRequestId) {
        return removeEnd(
                apiUrlProvider.find(IbanityProduct.Xs2a, "customer", "financialInstitution", "periodicPaymentInitiationRequests")
                        .replace(FinancialInstitution.API_URL_TAG_ID, financialInstitutionId)
                        .replace(PeriodicPaymentInitiationRequest.API_URL_TAG_ID, paymentInitiationRequestId),
                "/");
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class PeriodicPaymentInitiationRequest implements IbanityModel {

        public static final String RESOURCE_TYPE = "periodicPaymentInitiationRequest";
        public static final String API_URL_TAG_ID = "{paymentInitiationRequest" + URL_PARAMETER_ID_POSTFIX + "}";

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
        private String statusReason;
        private String redirectUri;
        private String locale;
        private String customerIpAddress;
        private boolean allowFinancialInstitutionRedirectUri;
        private boolean skipIbanityCompletionCallback;
        private String state;
        private String financialInstitutionCustomerReference;

        private BigDecimal amount;

        private LocalDate startDate;
        private LocalDate endDate;
        private String frequency;
        private String executionRule;
    }
}
