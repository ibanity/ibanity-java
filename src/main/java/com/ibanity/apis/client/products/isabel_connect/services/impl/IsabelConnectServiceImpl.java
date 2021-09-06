package com.ibanity.apis.client.products.isabel_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.http.OAuthHttpClient;
import com.ibanity.apis.client.http.handler.IbanityResponseHandler;
import com.ibanity.apis.client.products.isabel_connect.services.*;
import com.ibanity.apis.client.services.ApiUrlProvider;

public class IsabelConnectServiceImpl implements IsabelConnectService {
    private final AccountReportService accountReportService;
    private final AccountService accountService;
    private final BalanceService balanceService;
    private final BulkPaymentInitiationRequestService bulkPaymentInitiationRequestService;
    private final IntradayTransactionService intradayTransactionService;
    private final TransactionService transactionService;
    private final TokenService tokenService;

    public IsabelConnectServiceImpl(
            ApiUrlProvider apiUrlProvider,
            IbanityHttpClient ibanityHttpClient,
            OAuthHttpClient oAuthHttpClient) {
        accountReportService = new AccountReportServiceImpl(apiUrlProvider, ibanityHttpClient);
        accountService = new AccountServiceImpl(apiUrlProvider, ibanityHttpClient);
        balanceService = new BalanceServiceImpl(apiUrlProvider, ibanityHttpClient);
        intradayTransactionService = new IntradayTransactionServiceImpl(apiUrlProvider, ibanityHttpClient);
        transactionService = new TransactionServiceImpl(apiUrlProvider, ibanityHttpClient);
        tokenService = new TokenServiceImpl(apiUrlProvider, oAuthHttpClient);
        bulkPaymentInitiationRequestService = new BulkPaymentInitiationRequestServiceImpl(apiUrlProvider, new IbanityResponseHandler(), ibanityHttpClient);
    }

    @Override
    public AccountReportService accountReportService() { return accountReportService; }

    @Override
    public AccountService accountService() { return accountService; }

    @Override
    public BalanceService balanceService() { return balanceService; }

    @Override
    public BulkPaymentInitiationRequestService bulkPaymentInitiationRequestService() { return bulkPaymentInitiationRequestService; }

    @Override
    public IntradayTransactionService intradayTransactionService() { return intradayTransactionService; }

    @Override
    public TransactionService transactionService() { return transactionService; }

    @Override
    public TokenService tokenService() { return tokenService; }
}
