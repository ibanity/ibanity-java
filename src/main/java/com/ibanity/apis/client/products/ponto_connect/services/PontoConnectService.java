package com.ibanity.apis.client.products.ponto_connect.services;

public interface PontoConnectService {

    TokenService tokenService();

    AccountService accountService();

    TransactionService transactionService();

    FinancialInstitutionService financialInstitutionService();

    SynchronizationService synchronizationService();

    PaymentService paymentService();
}
