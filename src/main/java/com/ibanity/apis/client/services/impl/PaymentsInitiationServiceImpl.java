package com.ibanity.apis.client.services.impl;

import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.models.CustomerAccessToken;
import com.ibanity.apis.client.models.PaymentInitiationRequest;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.services.PaymentsInitiationService;
import io.crnk.core.exception.CrnkMappableException;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

public class PaymentsInitiationServiceImpl extends AbstractServiceImpl implements PaymentsInitiationService {
    private static final Logger LOGGER = LogManager.getLogger(PaymentsInitiationServiceImpl.class);

    private static final String PAYMENT_INITIATION_REQUESTS_PATH = "/customer/financial-institutions/" + FINANCIAL_INSTITUTION_ID_TAG;

    @Override
    public PaymentInitiationRequest initiatePaymentRequest(final CustomerAccessToken customerAccessToken, final PaymentInitiationRequest paymentInitiationRequest) {
        return getRepository(customerAccessToken, paymentInitiationRequest.getFinancialInstitution().getId(), null).create(paymentInitiationRequest);
    }

    @Override
    public PaymentInitiationRequest getPaymentInitiationRequest(final CustomerAccessToken customerAccessToken, final UUID financialInstitutionId, final UUID paymentInitiationRequestId) throws ApiErrorsException {
        QuerySpec querySpec = new QuerySpec(PaymentInitiationRequest.class);
        IbanityPagingSpec pagingSpec = new IbanityPagingSpec();
        querySpec.setPagingSpec(pagingSpec);
        try {
            return getRepository(customerAccessToken, financialInstitutionId, null).findOne(paymentInitiationRequestId, querySpec);
        } catch (CrnkMappableException e) {
            LOGGER.debug(e.getErrorData().toString());
            throw new ApiErrorsException(e.getHttpStatus(), e.getErrorData(), e);
        }
    }

    private ResourceRepositoryV2<PaymentInitiationRequest, UUID> getRepository(final CustomerAccessToken customerAccessToken, final UUID financialInstitutionId, final UUID idempotency) {
        String correctPath = PAYMENT_INITIATION_REQUESTS_PATH.replace(FINANCIAL_INSTITUTION_ID_TAG, financialInstitutionId.toString());
        return getApiClient(correctPath, customerAccessToken, idempotency).getRepositoryForType(PaymentInitiationRequest.class);
    }
}
