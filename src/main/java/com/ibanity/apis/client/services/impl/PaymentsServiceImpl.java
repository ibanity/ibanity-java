package com.ibanity.apis.client.services.impl;

import com.ibanity.apis.client.exceptions.ResourceNotFoundException;
import com.ibanity.apis.client.models.CustomerAccessToken;
import com.ibanity.apis.client.models.PaymentInitiationRequest;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.services.PaymentsService;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;

import java.util.UUID;

public class PaymentsServiceImpl extends AbstractServiceImpl implements PaymentsService {
    private static final String PAYMENT_INITIATION_REQUESTS_PATH = "/customer/financial-institutions/"+FINANCIAL_INSTITUTION_ID_TAG;

    @Override
    public PaymentInitiationRequest initiatePaymentRequest(CustomerAccessToken customerAccessToken, PaymentInitiationRequest paymentInitiationRequest) {
        return getRepository(customerAccessToken, paymentInitiationRequest.getFinancialInstitution().getId(), null).create(paymentInitiationRequest);
    }

    @Override
    public PaymentInitiationRequest getPaymentInitiationRequest(CustomerAccessToken customerAccessToken, UUID financialInstitutionId, UUID paymentInitiationRequestId) throws ResourceNotFoundException {
        QuerySpec querySpec = new QuerySpec(PaymentInitiationRequest.class);
        IbanityPagingSpec pagingSpec = new IbanityPagingSpec();
        querySpec.setPagingSpec(pagingSpec);
        try {
            return getRepository(customerAccessToken, financialInstitutionId, null).findOne(paymentInitiationRequestId, querySpec);
        } catch (io.crnk.core.exception.ResourceNotFoundException e) {
            String errorMessage = "Resource with provided ids not found";
            throw new ResourceNotFoundException(errorMessage);
        }
    }

    private ResourceRepositoryV2<PaymentInitiationRequest, UUID> getRepository(CustomerAccessToken customerAccessToken, UUID financialInstitutionId, UUID idempotency){
        String correctPath = PAYMENT_INITIATION_REQUESTS_PATH.replace(FINANCIAL_INSTITUTION_ID_TAG, financialInstitutionId.toString());
        return getApiClient(correctPath, customerAccessToken, idempotency).getRepositoryForType(PaymentInitiationRequest.class);
    }
}
