package com.ibanity.apis.client.products.xs2a.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.products.xs2a.sandbox.services.impl.SandboxServiceImpl;
import com.ibanity.apis.client.products.xs2a.services.*;
import com.ibanity.apis.client.services.ApiUrlProvider;

public class Xs2aServiceImpl implements Xs2aService {

    private final com.ibanity.apis.client.products.xs2a.sandbox.services.SandboxService sandboxService;
    private final AccountsService accountService;
    private final TransactionsService transactionService;
    private final TransactionDeleteRequestsService transactionDeleteRequestsService;
    private final PendingTransactionsService pendingTransactionService;
    private final SynchronizationService synchronizationService;
    private final BatchSynchronizationService batchSynchronizationService;
    private final BatchTransactionDeleteRequestsService batchTransactionDeleteRequestsService;
    private final CustomerAccessTokensService customerAccessTokensService;
    private final FinancialInstitutionsService financialInstitutionsService;
    private final BulkPaymentInitiationRequestService bulkPaymentInitiationRequestService;
    private final BulkPaymentInitiationRequestAuthorizationsServiceImpl bulkPaymentInitiationRequestAuthorizationsService;
    private final PaymentInitiationRequestService paymentInitiationRequestService;
    private final PaymentInitiationRequestAuthorizationsServiceImpl paymentInitiationRequestAuthorizationsService;
    private final PeriodicPaymentInitiationRequestService periodicPaymentInitiationRequestService;
    private final PeriodicPaymentInitiationRequestAuthorizationsServiceImpl periodicPaymentInitiationRequestAuthorizationsService;
    private final AccountInformationAccessRequestsService accountInformationAccessRequestsService;
    private final CustomerService customerService;
    private final HoldingsServiceImpl holdingsService;
    private final AuthorizationsServiceImpl authorizationsService;
    private final AccountInformationAccessRequestAuthorizationsServiceImpl accountInformationAccessRequestAuthorizationsService;
    private final FinancialInstitutionCountriesService financialInstitutionCountriesService;

    public Xs2aServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        accountService = new AccountsServiceImpl(apiUrlProvider, ibanityHttpClient);
        transactionService = new TransactionsServiceImpl(apiUrlProvider, ibanityHttpClient);
        transactionDeleteRequestsService = new TransactionDeleteRequestsServiceImpl(apiUrlProvider, ibanityHttpClient);
        pendingTransactionService = new PendingTransactionsServiceImpl(apiUrlProvider, ibanityHttpClient);
        synchronizationService = new SynchronizationServiceImpl(apiUrlProvider, ibanityHttpClient);
        batchSynchronizationService = new BatchSynchronizationServiceImpl(apiUrlProvider, ibanityHttpClient);
        batchTransactionDeleteRequestsService = new BatchTransactionDeleteRequestsServiceImpl(apiUrlProvider, ibanityHttpClient);
        customerAccessTokensService = new CustomerAccessTokensServiceImpl(apiUrlProvider, ibanityHttpClient);
        financialInstitutionsService = new FinancialInstitutionsServiceImpl(apiUrlProvider, ibanityHttpClient);
        bulkPaymentInitiationRequestService = new BulkPaymentInitiationRequestServiceImpl(apiUrlProvider, ibanityHttpClient);
        bulkPaymentInitiationRequestAuthorizationsService = new BulkPaymentInitiationRequestAuthorizationsServiceImpl(apiUrlProvider, ibanityHttpClient);
        paymentInitiationRequestService = new PaymentInitiationRequestServiceImpl(apiUrlProvider, ibanityHttpClient);
        paymentInitiationRequestAuthorizationsService = new PaymentInitiationRequestAuthorizationsServiceImpl(apiUrlProvider, ibanityHttpClient);
        periodicPaymentInitiationRequestService = new PeriodicPaymentInitiationRequestServiceImpl(apiUrlProvider, ibanityHttpClient);
        periodicPaymentInitiationRequestAuthorizationsService = new PeriodicPaymentInitiationRequestAuthorizationsServiceImpl(apiUrlProvider, ibanityHttpClient);
        accountInformationAccessRequestsService = new AccountInformationAccessRequestsServiceImpl(apiUrlProvider, ibanityHttpClient);
        accountInformationAccessRequestAuthorizationsService = new AccountInformationAccessRequestAuthorizationsServiceImpl(apiUrlProvider, ibanityHttpClient);
        financialInstitutionCountriesService = new FinancialInstitutionCountriesServiceImpl(apiUrlProvider, ibanityHttpClient);
        sandboxService = new SandboxServiceImpl(apiUrlProvider, ibanityHttpClient);
        customerService = new CustomerServiceImpl(apiUrlProvider, ibanityHttpClient);
        authorizationsService = new AuthorizationsServiceImpl(apiUrlProvider, ibanityHttpClient);
        holdingsService = new HoldingsServiceImpl(apiUrlProvider, ibanityHttpClient);
    }

    @Override
    public BulkPaymentInitiationRequestService bulkPaymentInitiationRequestService() {
        return bulkPaymentInitiationRequestService;
    }

    @Override
    public BulkPaymentInitiationRequestAuthorizationsService bulkPaymentInitiationRequestAuthorizationsService() {
        return bulkPaymentInitiationRequestAuthorizationsService;
    }

    @Override
    public PaymentInitiationRequestService paymentInitiationRequestService() {
        return paymentInitiationRequestService;
    }

    @Override
    public PaymentInitiationRequestAuthorizationsService paymentInitiationRequestAuthorizationsService() {
        return paymentInitiationRequestAuthorizationsService;
    }

    @Override
    public PeriodicPaymentInitiationRequestService periodicPaymentInitiationRequestService() {
        return periodicPaymentInitiationRequestService;
    }

    @Override
    public PeriodicPaymentInitiationRequestAuthorizationsService periodicPaymentInitiationRequestAuthorizationsService() {
        return periodicPaymentInitiationRequestAuthorizationsService;
    }

    @Override
    public FinancialInstitutionsService financialInstitutionsService() {
        return financialInstitutionsService;
    }

    @Override
    public CustomerAccessTokensService customerAccessTokensService() {
        return customerAccessTokensService;
    }

    @Override
    public AccountsService accountsService() {
        return accountService;
    }

    @Override
    public AccountInformationAccessRequestsService accountInformationAccessRequestsService() {
        return accountInformationAccessRequestsService;
    }

    @Override
    public TransactionsService transactionService() {
        return transactionService;
    }

    @Override
    public TransactionDeleteRequestsService transactionDeleteRequestsService() {
        return transactionDeleteRequestsService;
    }

    @Override
    public PendingTransactionsService pendingTransactionService() {
        return pendingTransactionService;
    }

    @Override
    public SynchronizationService synchronizationService() {
        return synchronizationService;
    }

    @Override
    public BatchSynchronizationService batchSynchronizationService() {
        return batchSynchronizationService;
    }

    @Override
    public BatchTransactionDeleteRequestsService batchTransactionDeleteRequestsService() {
        return batchTransactionDeleteRequestsService;
    }

    @Override
    public com.ibanity.apis.client.products.xs2a.sandbox.services.SandboxService sandbox() {
        return sandboxService;
    }

    @Override
    public CustomerService customerService() {
        return customerService;
    }

    /**
     * @deprecated  Replaced by {@link #accountInformationAccessRequestAuthorizationsService()}
     */
    @Deprecated
    @Override
    public AuthorizationsService authorizationsService() {
        return authorizationsService;
    }

    @Override
    public AccountInformationAccessRequestAuthorizationsService accountInformationAccessRequestAuthorizationsService() {
        return accountInformationAccessRequestAuthorizationsService;
    }

    @Override
    public HoldingsService holdingsService() {
        return holdingsService;
    }

    @Override
    public FinancialInstitutionCountriesService financialInstitutionCountriesService() {
        return financialInstitutionCountriesService;
    }
}
