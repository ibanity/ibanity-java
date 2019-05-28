package com.ibanity.apis.client.services.impl;

import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.models.PaymentInitiationRequest;
import com.ibanity.apis.client.models.factory.create.PaymentInitiationRequestCreationQuery;
import com.ibanity.apis.client.models.factory.read.PaymentInitiationRequestReadQuery;
import com.ibanity.apis.client.network.http.client.IbanityHttpClient;
import com.ibanity.apis.client.services.ApiUrlProvider;
import com.ibanity.apis.client.services.PaymentInitiationRequestService;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.buildRequest;
import static com.ibanity.apis.client.mappers.IbanityModelMapper.mapResource;
import static com.ibanity.apis.client.mappers.PaymentInitiationRequestMapper.getRequestMapping;
import static com.ibanity.apis.client.mappers.PaymentInitiationRequestMapper.getResponseMapping;
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
    public PaymentInitiationRequest create(final PaymentInitiationRequestCreationQuery query) {
        PaymentInitiationRequest paymentInitiationRequest = getRequestMapping(query);
        RequestApiModel request = buildRequest(PaymentInitiationRequest.RESOURCE_TYPE, paymentInitiationRequest);

        String url = getUrl(query.getFinancialInstitutionId().toString(), "");
        String response = ibanityHttpClient.post(buildUri(url), request, query.getCustomerAccessToken());

        return mapResource(response, getResponseMapping());
    }

    @Override
    public PaymentInitiationRequest delete(final PaymentInitiationRequestReadQuery paymentInitiationRequestReadQuery) {
        String financialInstitutionId = paymentInitiationRequestReadQuery.getFinancialInstitutionId().toString();
        String paymentInitiationRequestId = paymentInitiationRequestReadQuery.getPaymentInitiationRequestId().toString();

        String url = getUrl(financialInstitutionId, paymentInitiationRequestId);
        String response = ibanityHttpClient.delete(buildUri(url), paymentInitiationRequestReadQuery.getCustomerAccessToken());

        return mapResource(response, getResponseMapping());
    }

    @Override
    public PaymentInitiationRequest find(final PaymentInitiationRequestReadQuery paymentInitiationRequestReadQuery) {
        String financialInstitutionId = paymentInitiationRequestReadQuery.getFinancialInstitutionId().toString();
        String paymentInitiationRequestId = paymentInitiationRequestReadQuery.getPaymentInitiationRequestId().toString();

        String url = getUrl(financialInstitutionId, paymentInitiationRequestId);
        String response = ibanityHttpClient.get(buildUri(url), paymentInitiationRequestReadQuery.getCustomerAccessToken());

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
