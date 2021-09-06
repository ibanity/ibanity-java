package com.ibanity.apis.client.products.isabel_connect.services;

public interface IsabelConnectService {

    AccountReportService accountReportService();

    AccountService accountService();

    BalanceService balanceService();

    BulkPaymentInitiationRequestService bulkPaymentInitiationRequestService();

    IntradayTransactionService intradayTransactionService();

    TransactionService transactionService();

    TokenService tokenService();
}
