package com.ibanity.apis.client.products.xs2a.services;

import com.ibanity.apis.client.products.xs2a.sandbox.services.SandboxService;

public interface Xs2aService {

    PaymentInitiationRequestService paymentInitiationRequestService();

    FinancialInstitutionsService financialInstitutionsService();

    CustomerAccessTokensService customerAccessTokensService();

    AccountsService accountsService();

    AccountInformationAccessRequestsService accountInformationAccessRequestsService();

    TransactionsService transactionService();

    SynchronizationService synchronizationService();

    SandboxService sandbox();

    CustomerService customerService();

    /**
     * @deprecated  Replaced by {@link #accountInformationAccessRequestAuthorizationsService()}
     */
    @Deprecated
    AuthorizationsService authorizationsService();

    AccountInformationAccessRequestAuthorizationsService accountInformationAccessRequestAuthorizationsService();
}
