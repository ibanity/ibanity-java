package com.ibanity.apis.client.products.xs2a.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.DataApiModel;
import com.ibanity.apis.client.jsonapi.RelationshipsApiModel;
import com.ibanity.apis.client.mappers.IbanityModelMapper;
import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.products.xs2a.models.Account;
import com.ibanity.apis.client.products.xs2a.models.AccountInformationAccessRequest;
import com.ibanity.apis.client.products.xs2a.models.FinancialInstitution;
import com.ibanity.apis.client.products.xs2a.models.Synchronization;
import com.ibanity.apis.client.products.xs2a.models.delete.AccountDeleteQuery;
import com.ibanity.apis.client.products.xs2a.models.read.AccountReadQuery;
import com.ibanity.apis.client.products.xs2a.models.read.AccountsReadQuery;
import com.ibanity.apis.client.products.xs2a.services.AccountsService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;

import java.util.UUID;
import java.util.function.Function;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.toIbanityModel;
import static com.ibanity.apis.client.paging.IbanityPagingSpec.DEFAULT_PAGING_SPEC;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;

public class AccountsServiceImpl implements AccountsService {

    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public AccountsServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public Account find(AccountReadQuery accountReadQuery) {
            String url = getUrl(accountReadQuery.getFinancialInstitutionId(), null)
                    + "/"
                    + accountReadQuery.getAccountId();
            HttpResponse response = ibanityHttpClient.get(buildUri(url), accountReadQuery.getAdditionalHeaders(), accountReadQuery.getCustomerAccessToken());
            return IbanityModelMapper.mapResource(response, customMappingFunction());
    }

    @Override
    public IbanityCollection<Account> list(AccountsReadQuery accountsReadQuery) {
        IbanityPagingSpec pagingSpec = accountsReadQuery.getPagingSpec();

        if (pagingSpec == null) {
            pagingSpec = DEFAULT_PAGING_SPEC;
        }

        String url = getUrl(accountsReadQuery.getFinancialInstitutionId(), accountsReadQuery.getAccountInformationAccessRequestId());
        HttpResponse response = ibanityHttpClient.get(buildUri(url, pagingSpec), accountsReadQuery.getAdditionalHeaders(), accountsReadQuery.getCustomerAccessToken());
        return IbanityModelMapper.mapCollection(response, customMappingFunction());
    }

    @Override
    public Account delete(AccountDeleteQuery accountDeleteQuery) {
        String url = getUrl(accountDeleteQuery.getFinancialInstitutionId(), null)
                + "/"
                + accountDeleteQuery.getAccountId();
        HttpResponse response = ibanityHttpClient.delete(buildUri(url), accountDeleteQuery.getAdditionalHeaders(), accountDeleteQuery.getCustomerAccessToken());
        return IbanityModelMapper.mapResource(response, customMappingFunction());
    }

    private Function<DataApiModel, Account> customMappingFunction() {
        return dataApiModel -> {
            Account account = toIbanityModel(dataApiModel, Account.class);
            if(dataApiModel.getMeta() != null) {
                Synchronization synchronization = toIbanityModel(dataApiModel.getMeta().getLatestSynchronization(), Synchronization.class);
                account.setLatestSynchronization(synchronization);
                account.setSynchronizedAt(dataApiModel.getMeta().getSynchronizedAt());
            }

            RelationshipsApiModel financialInstitution = dataApiModel.getRelationships().get("financialInstitution");
            if(financialInstitution != null) {
                account.setFinancialInstitutionId(financialInstitution.getData().getId());
            }

            return account;
        };
    }

    private String getUrl(UUID financialInstitutionId, UUID accountInformationAccessRequestId) {
        String url;
        if (accountInformationAccessRequestId != null && financialInstitutionId != null) {
            url = apiUrlProvider.find(IbanityProduct.Xs2a, "customer", "financialInstitution", "accountInformationAccessRequest", "accounts")
                    .replace(FinancialInstitution.API_URL_TAG_ID, financialInstitutionId.toString())
                    .replace(AccountInformationAccessRequest.API_URL_TAG_ID, accountInformationAccessRequestId.toString())
                    .replace(Account.API_URL_TAG_ID, "");
        } else if (financialInstitutionId != null) {
            url = apiUrlProvider.find(IbanityProduct.Xs2a, "customer", "financialInstitution", "accounts")
                    .replace(FinancialInstitution.API_URL_TAG_ID, financialInstitutionId.toString())
                    .replace(Account.API_URL_TAG_ID, "");
        } else {
            url = apiUrlProvider.find(IbanityProduct.Xs2a, "customer", "accounts")
                    .replace(Account.API_URL_TAG_ID, "");
        }

        return StringUtils.removeEnd(url, "/");
    }
}
