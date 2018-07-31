package com.ibanity.apis.client.services.impl;

import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.models.PaymentInitiationRequest;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.services.PaymentsInitiationService;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class PaymentsInitiationServiceImpl extends AbstractServiceImpl implements PaymentsInitiationService {

    @Override
    public PaymentInitiationRequest create(final String customerAccessToken, final PaymentInitiationRequest paymentInitiationRequest) {
        return create(customerAccessToken, paymentInitiationRequest, null);
    }

    @Override
    public PaymentInitiationRequest create(
            final String customerAccessToken, final PaymentInitiationRequest paymentInitiationRequest, final UUID idempotencyKey) {
        return getRepository(customerAccessToken, paymentInitiationRequest.getFinancialInstitution().getId(), idempotencyKey)
                .create(paymentInitiationRequest);
    }

    @Override
    public PaymentInitiationRequest find(
            final String customerAccessToken, final UUID financialInstitutionId, final UUID paymentInitiationRequestId) {

        QuerySpec querySpec = new QuerySpec(PaymentInitiationRequest.class)
                .setPagingSpec(new IbanityPagingSpec());

        return getRepository(customerAccessToken, financialInstitutionId, null)
                .findOne(paymentInitiationRequestId, querySpec);
    }

    private ResourceRepositoryV2<PaymentInitiationRequest, UUID> getRepository(
            final String customerAccessToken, final UUID financialInstitutionId, final UUID idempotencyKey) {
        String finalPath = StringUtils.removeEnd(
                IbanityConfiguration.getApiUrls().getCustomer().getFinancialInstitution().getPaymentInitiationRequests()
                        .replace(FinancialInstitution.API_URL_TAG_ID, financialInstitutionId.toString())
                        .replace(PaymentInitiationRequest.RESOURCE_PATH, "")
                        .replace(PaymentInitiationRequest.API_URL_TAG_ID, ""),
                "//");

        return getApiClient(finalPath, customerAccessToken, idempotencyKey).getRepositoryForType(PaymentInitiationRequest.class);
    }
}
