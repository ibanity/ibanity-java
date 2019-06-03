package com.ibanity.apis.client.products.xs2a.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.products.xs2a.sandbox.services.impl.SandboxServiceImpl;
import com.ibanity.apis.client.products.xs2a.services.AccountInformationAccessRequestsService;
import com.ibanity.apis.client.products.xs2a.services.AccountsService;
import com.ibanity.apis.client.products.xs2a.services.CustomerAccessTokensService;
import com.ibanity.apis.client.products.xs2a.services.FinancialInstitutionsService;
import com.ibanity.apis.client.products.xs2a.services.PaymentInitiationRequestService;
import com.ibanity.apis.client.products.xs2a.services.SynchronizationService;
import com.ibanity.apis.client.products.xs2a.services.TransactionsService;
import com.ibanity.apis.client.services.ApiUrlProvider;

public class Xs2aServiceImpl implements com.ibanity.apis.client.products.xs2a.services.Xs2aService {

    private final com.ibanity.apis.client.products.xs2a.sandbox.services.SandboxService sandboxService;
    private final AccountsService accountService;
    private final TransactionsService transactionService;
    private final SynchronizationService synchronizationService;
    private final CustomerAccessTokensService customerAccessTokensService;
    private final FinancialInstitutionsService financialInstitutionsService;
    private final PaymentInitiationRequestService paymentInitiationRequestService;
    private final AccountInformationAccessRequestsService accountInformationAccessRequestsService;

    public Xs2aServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        accountService = new AccountsServiceImpl(apiUrlProvider, ibanityHttpClient);
        transactionService = new TransactionsServiceImpl(apiUrlProvider, ibanityHttpClient);
        synchronizationService = new SynchronizationServiceImpl(apiUrlProvider, ibanityHttpClient);
        customerAccessTokensService = new CustomerAccessTokensServiceImpl(apiUrlProvider, ibanityHttpClient);
        financialInstitutionsService = new FinancialInstitutionsServiceImpl(apiUrlProvider, ibanityHttpClient);
        paymentInitiationRequestService = new PaymentInitiationRequestServiceImpl(apiUrlProvider, ibanityHttpClient);
        accountInformationAccessRequestsService = new AccountInformationAccessRequestsServiceImpl(apiUrlProvider, ibanityHttpClient);
        sandboxService = new SandboxServiceImpl(apiUrlProvider, ibanityHttpClient);
    }

    @Override
    public PaymentInitiationRequestService paymentInitiationRequestService() {
        return paymentInitiationRequestService;
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
    public SynchronizationService synchronizationService() {
        return synchronizationService;
    }

    @Override
    public com.ibanity.apis.client.products.xs2a.sandbox.services.SandboxService sandbox() {
        return sandboxService;
    }
}
