package com.ibanity.apis.client.products.ponto_connect.sandbox.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.products.ponto_connect.sandbox.models.FinancialInstitutionAccount;
import com.ibanity.apis.client.products.ponto_connect.sandbox.models.factory.read.FinancialInstitutionAccountReadQuery;
import com.ibanity.apis.client.products.ponto_connect.sandbox.models.factory.read.FinancialInstitutionAccountsReadQuery;
import com.ibanity.apis.client.products.ponto_connect.sandbox.services.FinancialInstitutionAccountsService;
import com.ibanity.apis.client.products.xs2a.models.FinancialInstitution;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.apache.http.HttpResponse;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.mapCollection;
import static com.ibanity.apis.client.mappers.IbanityModelMapper.mapResource;
import static com.ibanity.apis.client.models.IbanityProduct.PontoConnect;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;
import static org.apache.commons.lang3.StringUtils.removeEnd;

public class FinancialInstitutionAccountsServiceImpl implements FinancialInstitutionAccountsService {

    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public FinancialInstitutionAccountsServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public FinancialInstitutionAccount find(FinancialInstitutionAccountReadQuery accountReadQuery) {
        String url =
                getUrl(accountReadQuery.getFinancialInstitutionId().toString(),
                        accountReadQuery.getFinancialInstitutionAccountId().toString());

        HttpResponse response = ibanityHttpClient.get(buildUri(url), accountReadQuery.getAccessToken());
        return mapResource(response, FinancialInstitutionAccount.class);
    }

    @Override
    public IbanityCollection<FinancialInstitutionAccount> list(FinancialInstitutionAccountsReadQuery accountsReadQuery) {
        String url = getUrl(accountsReadQuery.getFinancialInstitutionId().toString(), "");

        HttpResponse response = ibanityHttpClient.get(buildUri(url, accountsReadQuery.getPagingSpec()), accountsReadQuery.getAccessToken());
        return mapCollection(response, FinancialInstitutionAccount.class);
    }

    private String getUrl(String financialInstitutionId, String financialInstitutionAccountId) {
        return removeEnd(apiUrlProvider.find(PontoConnect, "sandbox", "financialInstitution", "financialInstitutionAccounts")
                .replace(FinancialInstitution.API_URL_TAG_ID, financialInstitutionId)
                .replace(FinancialInstitutionAccount.API_URL_TAG_ID, financialInstitutionAccountId), "/");
    }
}
