package com.ibanity.apis.client.products.ponto_connect.services;

import com.ibanity.apis.client.products.ponto_connect.sandbox.services.SandboxService;

public interface PontoConnectService {

    TokenService tokenService();

    AccountService accountService();

    TransactionService transactionService();

    PendingTransactionService pendingTransactionService();

    FinancialInstitutionService financialInstitutionService();

    SynchronizationService synchronizationService();

    PaymentService paymentService();

    SandboxService sandboxService();

    UserinfoService userinfoService();

    UsageService usageService();

    IntegrationService integrationService();

    OnboardingDetailsService onboardingDetailsService();

    BulkPaymentService bulkPaymentService();

    ReauthorizationRequestService reauthorizationRequestService();

    PaymentActivationRequestService paymentActivationRequestService();

    IntegrationAccountService integrationAccountService();

    PaymentRequestService paymentRequestService();
}
