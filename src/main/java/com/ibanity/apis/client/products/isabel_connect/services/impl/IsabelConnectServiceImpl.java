package com.ibanity.apis.client.products.isabel_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.http.OAuthHttpClient;
import com.ibanity.apis.client.products.isabel_connect.services.*;
import com.ibanity.apis.client.services.ApiUrlProvider;

public class IsabelConnectServiceImpl implements IsabelConnectService {
    private final AccountReportService accountReportService;
    private final AccountsService accountsService;
    private final BalanceService balanceService;
    private final IntradayTransactionService intradayTransactionService;
    private final TransactionService transactionService;
    private final TokenService tokenService;

    public IsabelConnectServiceImpl(
            ApiUrlProvider apiUrlProvider,
            IbanityHttpClient ibanityHttpClient,
            OAuthHttpClient oAuthHttpClient) {
        accountReportService = new AccountReportServiceImpl(apiUrlProvider, ibanityHttpClient);
        accountsService = new AccountsServiceImpl(apiUrlProvider, ibanityHttpClient);
        balanceService = new BalanceServiceImpl(apiUrlProvider, ibanityHttpClient);
        intradayTransactionService = new IntradayTransactionServiceImpl(apiUrlProvider, ibanityHttpClient);
        transactionService = new TransactionServiceImpl(apiUrlProvider, ibanityHttpClient);
        tokenService = new TokenServiceImpl(apiUrlProvider, oAuthHttpClient);
    }

    @Override
    public AccountReportService accountReportService() { return accountReportService; }

    @Override
    public AccountsService accountsService() { return accountsService; }

    @Override
    public BalanceService balanceService() { return balanceService; }

    @Override
    public IntradayTransactionService intradayTransactionService() { return intradayTransactionService; }

    @Override
    public TransactionService transactionService() { return transactionService; }

    @Override
    public TokenService tokenService() { return tokenService; }
}
