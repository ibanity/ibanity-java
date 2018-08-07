package com.ibanity.apis.client.services.impl;

import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.models.PaymentInitiationRequest;
import com.ibanity.apis.client.models.factory.create.PaymentInitiationRequestCreationQuery;
import com.ibanity.apis.client.models.factory.read.PaymentInitiationRequestReadQuery;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.services.PaymentInitiationRequestService;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class PaymentInitiationRequestServiceImpl extends AbstractServiceImpl implements PaymentInitiationRequestService {

    @Override
    public PaymentInitiationRequest create(final PaymentInitiationRequestCreationQuery query) {

        PaymentInitiationRequest paymentInitiationRequest = new PaymentInitiationRequest();

        FinancialInstitution financialInstitution = new FinancialInstitution();
        financialInstitution.setId(query.getFinancialInstitutionId());

        paymentInitiationRequest.setFinancialInstitution(financialInstitution);
        paymentInitiationRequest.setAmount(query.getAmount());
        paymentInitiationRequest.setConsentReference(query.getConsentReference());
        paymentInitiationRequest.setCreditorAccountReference(query.getCreditorAccountReference());
        paymentInitiationRequest.setCreditorAccountReferenceType(query.getCreditorAccountReferenceType());
        paymentInitiationRequest.setCreditorName(query.getCreditorName());
        paymentInitiationRequest.setCurrency(query.getCurrency());
        paymentInitiationRequest.setEndToEndId(query.getEndToEndId());
        paymentInitiationRequest.setProductType(query.getProductType());
        paymentInitiationRequest.setRedirectUri(query.getRedirectUri());
        paymentInitiationRequest.setRemittanceInformation(query.getRemittanceInformation());
        paymentInitiationRequest.setRemittanceInformationType(query.getRemittanceInformationType());
        paymentInitiationRequest.setCreditorAgent(query.getCreditorAgent());
        paymentInitiationRequest.setCreditorAgentType(query.getCreditorAgentType());
        paymentInitiationRequest.setDebtorAccountReference(query.getDebtorAccountReference());
        paymentInitiationRequest.setDebtorAccountReferenceType(query.getDebtorAccountReferenceType());
        paymentInitiationRequest.setDebtorName(query.getDebtorName());

        return getRepository(query.getCustomerAccessToken(), query.getFinancialInstitutionId(), query.getIdempotencyKey())
                .create(paymentInitiationRequest);
    }

    @Override
    public PaymentInitiationRequest find(final PaymentInitiationRequestReadQuery paymentInitiationRequestReadQuery) {
        QuerySpec querySpec = new QuerySpec(PaymentInitiationRequest.class)
                .setPagingSpec(new IbanityPagingSpec());

        return getRepository(paymentInitiationRequestReadQuery.getCustomerAccessToken(),
                    paymentInitiationRequestReadQuery.getFinancialInstitutionId(),
                    null)
                .findOne(paymentInitiationRequestReadQuery.getPaymentInitiationRequestId(), querySpec);
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
