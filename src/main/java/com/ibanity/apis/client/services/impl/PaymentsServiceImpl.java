package com.ibanity.apis.client.services.impl;

import com.ibanity.apis.client.models.CustomerAccessToken;
import com.ibanity.apis.client.models.PaymentInitiationRequest;
import com.ibanity.apis.client.paging.IBanityPagingSpec;
import com.ibanity.apis.client.services.PaymentsService;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;

import java.util.UUID;

public class PaymentsServiceImpl extends AbstractServiceImpl implements PaymentsService {
    private static final String PAYMENT_INITIATION_REQUESTS_PATH = "/customer/financial-institutions/"+FINANCIAL_INSTITUTION_ID_TAG;

    @Override
    public PaymentInitiationRequest initiatePaymentRequest(CustomerAccessToken customerAccessToken, PaymentInitiationRequest paymentInitiationRequest) {
        return getRepository(customerAccessToken, paymentInitiationRequest.getFinancialInstitution().getId()).create(paymentInitiationRequest);
    }

    @Override
    public PaymentInitiationRequest getPaymentInitiationRequest(CustomerAccessToken customerAccessToken, UUID financialInstitutionId, UUID paymentInitiationRequestId) {
        QuerySpec querySpec = new QuerySpec(PaymentInitiationRequest.class);
        IBanityPagingSpec pagingSpec = new IBanityPagingSpec();
        querySpec.setPagingSpec(pagingSpec);
        return getRepository(customerAccessToken, financialInstitutionId).findOne(paymentInitiationRequestId, querySpec);
    }

    private ResourceRepositoryV2<PaymentInitiationRequest, UUID> getRepository(CustomerAccessToken customerAccessToken, UUID financialInstitutionId){
        String correctPath = PAYMENT_INITIATION_REQUESTS_PATH.replace(FINANCIAL_INSTITUTION_ID_TAG, financialInstitutionId.toString());
        return getApiClient(correctPath, customerAccessToken).getRepositoryForType(PaymentInitiationRequest.class);
    }
}
