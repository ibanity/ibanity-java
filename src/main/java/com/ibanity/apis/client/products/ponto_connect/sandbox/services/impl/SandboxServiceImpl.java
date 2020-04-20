package com.ibanity.apis.client.products.ponto_connect.sandbox.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.products.ponto_connect.sandbox.services.FinancialInstitutionAccountsService;
import com.ibanity.apis.client.products.ponto_connect.sandbox.services.FinancialInstitutionTransactionsService;
import com.ibanity.apis.client.products.ponto_connect.sandbox.services.SandboxService;
import com.ibanity.apis.client.services.ApiUrlProvider;

public class SandboxServiceImpl implements SandboxService {

    private final FinancialInstitutionAccountsService financialInstitutionAccountsService;
    private final FinancialInstitutionTransactionsService financialInstitutionTransactionsService;

    public SandboxServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        financialInstitutionAccountsService = new FinancialInstitutionAccountsServiceImpl(apiUrlProvider, ibanityHttpClient);
        financialInstitutionTransactionsService = new FinancialInstitutionTransactionsServiceImpl(apiUrlProvider, ibanityHttpClient);
    }

    @Override
    public FinancialInstitutionAccountsService financialInstitutionAccountsService() {
        return financialInstitutionAccountsService;
    }

    @Override
    public FinancialInstitutionTransactionsService financialInstitutionTransactionsService() {
        return financialInstitutionTransactionsService;
    }
}
