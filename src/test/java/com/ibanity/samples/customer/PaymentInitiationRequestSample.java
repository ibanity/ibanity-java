package com.ibanity.samples.customer;

import com.ibanity.apis.client.models.CustomerAccessToken;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.models.PaymentInitiationRequest;
import com.ibanity.apis.client.models.factory.create.PaymentInitiationRequestCreationQuery;
import com.ibanity.apis.client.services.PaymentInitiationRequestService;
import com.ibanity.apis.client.services.impl.PaymentInitiationRequestServiceImpl;
import com.ibanity.samples.helper.SampleHelper;

public class PaymentInitiationRequestSample {
    private final PaymentInitiationRequestService paymentInitiationRequestService = new PaymentInitiationRequestServiceImpl();

    public PaymentInitiationRequest create(FinancialInstitution financialInstitution, CustomerAccessToken customerAccessToken,
                                           String redirectUrl){
        PaymentInitiationRequestCreationQuery paymentInitiationRequestCreationQuery =
                SampleHelper.generateRandomPaymentInitiationRequestCreationQuery(
                        financialInstitution, customerAccessToken, redirectUrl);

        return paymentInitiationRequestService.create(paymentInitiationRequestCreationQuery);
    }
}
