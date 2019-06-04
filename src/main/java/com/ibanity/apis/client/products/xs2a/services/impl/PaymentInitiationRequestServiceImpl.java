package com.ibanity.apis.client.products.xs2a.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.xs2a.models.FinancialInstitution;
import com.ibanity.apis.client.products.xs2a.models.PaymentInitiationRequest;
import com.ibanity.apis.client.products.xs2a.models.create.PaymentInitiationRequestCreationQuery;
import com.ibanity.apis.client.products.xs2a.models.read.PaymentInitiationRequestReadQuery;
import com.ibanity.apis.client.products.xs2a.services.PaymentInitiationRequestService;
import com.ibanity.apis.client.services.ApiUrlProvider;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.buildRequest;
import static com.ibanity.apis.client.mappers.IbanityModelMapper.mapResource;
import static com.ibanity.apis.client.products.xs2a.mappers.PaymentInitiationRequestMapper.getRequestMapping;
import static com.ibanity.apis.client.products.xs2a.mappers.PaymentInitiationRequestMapper.getResponseMapping;
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
    public PaymentInitiationRequest create(PaymentInitiationRequestCreationQuery query) {
        PaymentInitiationRequest paymentInitiationRequest = getRequestMapping(query);
        RequestApiModel request = buildRequest(PaymentInitiationRequest.RESOURCE_TYPE, paymentInitiationRequest);

        String url = getUrl(query.getFinancialInstitutionId().toString(), "");
        String response = ibanityHttpClient.post(buildUri(url), request, query.getAdditionalHeaders(), query.getCustomerAccessToken());

        return mapResource(response, getResponseMapping());
    }

    @Override
    public PaymentInitiationRequest delete(PaymentInitiationRequestReadQuery paymentInitiationRequestReadQuery) {
        String financialInstitutionId = paymentInitiationRequestReadQuery.getFinancialInstitutionId().toString();
        String paymentInitiationRequestId = paymentInitiationRequestReadQuery.getPaymentInitiationRequestId().toString();

        String url = getUrl(financialInstitutionId, paymentInitiationRequestId);
        String response = ibanityHttpClient.delete(buildUri(url), paymentInitiationRequestReadQuery.getAdditionalHeaders(), paymentInitiationRequestReadQuery.getCustomerAccessToken());

        return mapResource(response, getResponseMapping());
    }

    @Override
    public PaymentInitiationRequest find(PaymentInitiationRequestReadQuery paymentInitiationRequestReadQuery) {
        String financialInstitutionId = paymentInitiationRequestReadQuery.getFinancialInstitutionId().toString();
        String paymentInitiationRequestId = paymentInitiationRequestReadQuery.getPaymentInitiationRequestId().toString();

        String url = getUrl(financialInstitutionId, paymentInitiationRequestId);
        String response = ibanityHttpClient.get(buildUri(url), paymentInitiationRequestReadQuery.getAdditionalHeaders(), paymentInitiationRequestReadQuery.getCustomerAccessToken());

        return mapResource(response, getResponseMapping());
    }

    private String getUrl(String financialInstitutionId, String paymentInitiationRequestId) {
        return removeEnd(
                apiUrlProvider.find(IbanityProduct.Xs2a, "customer", "financialInstitution", "paymentInitiationRequests")
                        .replace(FinancialInstitution.API_URL_TAG_ID, financialInstitutionId)
                        .replace(PaymentInitiationRequest.API_URL_TAG_ID, paymentInitiationRequestId),
                "/");
    }
}
