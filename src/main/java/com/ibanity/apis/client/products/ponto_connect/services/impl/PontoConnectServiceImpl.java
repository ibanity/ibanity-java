package com.ibanity.apis.client.products.ponto_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.http.OAuthHttpClient;
import com.ibanity.apis.client.products.ponto_connect.sandbox.services.SandboxService;
import com.ibanity.apis.client.products.ponto_connect.sandbox.services.impl.SandboxServiceImpl;
import com.ibanity.apis.client.products.ponto_connect.services.*;
import com.ibanity.apis.client.services.ApiUrlProvider;

public class PontoConnectServiceImpl implements PontoConnectService {

    private final TokenService tokenService;
    private final UsageService usageService;
    private final AccountService accountService;
    private final UserinfoService userinfoService;
    private final TransactionService transactionService;
    private final SynchronizationService synchronizationService;
    private final FinancialInstitutionService financialInstitutionService;
    private final PaymentService paymentService;
    private final IntegrationService integrationService;
    private final SandboxService sandboxService;
    private final OnboardingDetailsService onboardingDetailsService;
    private final BulkPaymentService bulkPaymentService;
    private final ReauthorizationRequestService reauthorizationRequestService;
    private final PaymentActivationRequestService paymentActivationRequestService;
    private final IntegrationAccountService integrationAccountService;

    public PontoConnectServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient, OAuthHttpClient oAuthHttpClient) {
        tokenService = new TokenServiceImpl(apiUrlProvider, oAuthHttpClient);
        usageService = new UsageServiceImpl(apiUrlProvider, ibanityHttpClient);
        accountService = new AccountServiceImpl(apiUrlProvider, ibanityHttpClient);
        userinfoService = new UserinfoServiceImpl(apiUrlProvider, ibanityHttpClient);
        transactionService = new TransactionServiceImpl(apiUrlProvider, ibanityHttpClient);
        synchronizationService = new SynchronizationServiceImpl(apiUrlProvider, ibanityHttpClient);
        financialInstitutionService = new FinancialInstitutionServiceImpl(apiUrlProvider, ibanityHttpClient);
        paymentService = new PaymentServiceImpl(apiUrlProvider, ibanityHttpClient);
        sandboxService = new SandboxServiceImpl(apiUrlProvider, ibanityHttpClient);
        integrationService = new IntegrationServiceImpl(apiUrlProvider, ibanityHttpClient);
        onboardingDetailsService = new OnboardingDetailsServiceImpl(apiUrlProvider, ibanityHttpClient);
        bulkPaymentService = new BulkPaymentServiceImpl(apiUrlProvider, ibanityHttpClient);
        reauthorizationRequestService = new ReauthorizationRequestServiceImpl(apiUrlProvider, ibanityHttpClient);
        paymentActivationRequestService = new PaymentActivationRequestServiceImpl(apiUrlProvider, ibanityHttpClient);
        integrationAccountService = new IntegrationAccountServiceImpl(apiUrlProvider, ibanityHttpClient);
    }

    @Override
    public TokenService tokenService() {
        return tokenService;
    }

    @Override
    public AccountService accountService() {
        return accountService;
    }

    @Override
    public TransactionService transactionService() {
        return transactionService;
    }

    @Override
    public FinancialInstitutionService financialInstitutionService() {
        return financialInstitutionService;
    }

    @Override
    public SynchronizationService synchronizationService() {
        return synchronizationService;
    }

    @Override
    public PaymentService paymentService() {
        return paymentService;
    }

    @Override
    public SandboxService sandboxService() {
        return sandboxService;
    }

    @Override
    public UserinfoService userinfoService() {
        return userinfoService;
    }

    @Override
    public UsageService usageService() {
        return usageService;
    }

    @Override
    public IntegrationService integrationService() {
        return integrationService;
    }

    @Override
    public OnboardingDetailsService onboardingDetailsService() {
        return onboardingDetailsService;
    }

    @Override
    public BulkPaymentService bulkPaymentService() {
        return bulkPaymentService;
    }

    @Override
    public ReauthorizationRequestService reauthorizationRequestService() {
        return reauthorizationRequestService;
    }

    @Override
    public PaymentActivationRequestService paymentActivationRequestService() { return paymentActivationRequestService;}

    @Override
    public IntegrationAccountService integrationAccountService() {
        return integrationAccountService;
    }
}
