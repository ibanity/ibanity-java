package com.ibanity.samples.customer;

import com.ibanity.apis.client.helpers.IbanityService;
import com.ibanity.apis.client.products.xs2a.models.CustomerAccessToken;
import com.ibanity.apis.client.products.xs2a.models.FinancialInstitution;
import com.ibanity.apis.client.products.xs2a.models.PaymentInitiationRequest;
import com.ibanity.apis.client.products.xs2a.models.factory.create.PaymentInitiationRequestCreationQuery;
import com.ibanity.apis.client.products.xs2a.services.PaymentInitiationRequestService;
import com.ibanity.apis.client.products.xs2a.services.impl.PaymentInitiationRequestServiceImpl;
import com.ibanity.samples.helper.SampleHelper;

public class PaymentInitiationRequestSample {

    private final PaymentInitiationRequestService paymentInitiationRequestService;

    public PaymentInitiationRequestSample(IbanityService ibanityService) {
        paymentInitiationRequestService = new PaymentInitiationRequestServiceImpl(ibanityService.apiUrlProvider(), ibanityService.ibanityHttpClient());
    }

    public PaymentInitiationRequest create(FinancialInstitution financialInstitution, CustomerAccessToken customerAccessToken,
                                           String redirectUrl){
        PaymentInitiationRequestCreationQuery paymentInitiationRequestCreationQuery =
                SampleHelper.generateRandomPaymentInitiationRequestCreationQuery(
                        financialInstitution, customerAccessToken, redirectUrl);

        return paymentInitiationRequestService.create(paymentInitiationRequestCreationQuery);
    }
}
