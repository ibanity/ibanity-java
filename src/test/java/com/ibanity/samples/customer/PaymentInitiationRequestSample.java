package com.ibanity.samples.customer;

import com.ibanity.apis.client.products.xs2a.models.CustomerAccessToken;
import com.ibanity.apis.client.products.xs2a.models.FinancialInstitution;
import com.ibanity.apis.client.products.xs2a.models.PaymentInitiationRequest;
import com.ibanity.apis.client.products.xs2a.models.create.PaymentInitiationRequestCreationQuery;
import com.ibanity.apis.client.products.xs2a.services.PaymentInitiationRequestService;
import com.ibanity.apis.client.services.IbanityService;
import com.ibanity.samples.helper.SampleHelper;

public class PaymentInitiationRequestSample {

    private final PaymentInitiationRequestService paymentInitiationRequestService;

    public PaymentInitiationRequestSample(IbanityService ibanityService) {
        paymentInitiationRequestService = ibanityService.xs2aService().paymentInitiationRequestService();
    }

    public PaymentInitiationRequest create(FinancialInstitution financialInstitution, CustomerAccessToken customerAccessToken,
                                           String redirectUrl){
        PaymentInitiationRequestCreationQuery paymentInitiationRequestCreationQuery =
                SampleHelper.generateRandomPaymentInitiationRequestCreationQuery(
                        financialInstitution, customerAccessToken, redirectUrl);

        return paymentInitiationRequestService.create(paymentInitiationRequestCreationQuery);
    }
}
