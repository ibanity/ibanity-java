package com.ibanity.apis.client.products.ponto_connect.services;

import com.ibanity.apis.client.products.ponto_connect.sandbox.services.SandboxService;

public interface PontoConnectService {

    TokenService tokenService();

    AccountService accountService();

    TransactionService transactionService();

    FinancialInstitutionService financialInstitutionService();

    SynchronizationService synchronizationService();

    PaymentService paymentService();

    SandboxService sandboxService();
}
