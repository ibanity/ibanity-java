package com.ibanity.api.impl;

import com.ibanity.api.PaymentsService;
import com.ibanity.models.CustomerAccessToken;
import com.ibanity.models.PaymentInitiationRequest;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

public class PaymentsServiceImpl extends AbstractServiceImpl implements PaymentsService{
    private static final Logger LOGGER = LogManager.getLogger(PaymentsServiceImpl.class);
    private static final String PAYMENT_INITIATION_REQUESTS_PATH = "/customer/financial-institutions/"+FINANCIAL_INSTITUTION_ID_TAG;

    @Override
    public PaymentInitiationRequest initiatePaymentRequest(CustomerAccessToken customerAccessToken, PaymentInitiationRequest paymentInitiationRequest) {
        return getRepository(customerAccessToken, paymentInitiationRequest.getFinancialInstitution().getId()).create(paymentInitiationRequest);
    }

    @Override
    public PaymentInitiationRequest getPaymentInitiationRequest(CustomerAccessToken customerAccessToken, UUID financialInstitutionId, UUID paymentInitiationRequestId) {
        return getRepository(customerAccessToken, financialInstitutionId).findOne(paymentInitiationRequestId, new QuerySpec(PaymentInitiationRequest.class));
    }

    private ResourceRepositoryV2<PaymentInitiationRequest, UUID> getRepository(CustomerAccessToken customerAccessToken, UUID financialInstitutionId){
        String correctPath = PAYMENT_INITIATION_REQUESTS_PATH.replace(FINANCIAL_INSTITUTION_ID_TAG, financialInstitutionId.toString());
        return getApiClient(correctPath, customerAccessToken).getRepositoryForType(PaymentInitiationRequest.class);
    }
}
