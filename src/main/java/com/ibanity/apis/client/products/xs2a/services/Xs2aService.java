package com.ibanity.apis.client.products.xs2a.services;

import com.ibanity.apis.client.products.xs2a.sandbox.services.SandboxService;

public interface Xs2aService {

    BulkPaymentInitiationRequestService bulkPaymentInitiationRequestService();

    BulkPaymentInitiationRequestAuthorizationsService bulkPaymentInitiationRequestAuthorizationsService();

    PaymentInitiationRequestService paymentInitiationRequestService();

    PaymentInitiationRequestAuthorizationsService paymentInitiationRequestAuthorizationsService();

    PeriodicPaymentInitiationRequestService periodicPaymentInitiationRequestService();

    PeriodicPaymentInitiationRequestAuthorizationsService periodicPaymentInitiationRequestAuthorizationsService();

    FinancialInstitutionsService financialInstitutionsService();

    CustomerAccessTokensService customerAccessTokensService();

    AccountsService accountsService();

    AccountInformationAccessRequestsService accountInformationAccessRequestsService();

    PendingTransactionsService pendingTransactionService();

    TransactionsService transactionService();

    BatchSynchronizationService batchSynchronizationService();

    SynchronizationService synchronizationService();

    SandboxService sandbox();

    CustomerService customerService();

    /**
     * @deprecated  Replaced by {@link #accountInformationAccessRequestAuthorizationsService()}
     */
    @Deprecated
    AuthorizationsService authorizationsService();

    AccountInformationAccessRequestAuthorizationsService accountInformationAccessRequestAuthorizationsService();

    HoldingsService holdingsService();

    FinancialInstitutionCountriesService financialInstitutionCountriesService();
}
