package com.ibanity.apis.client.products.ponto_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.DataApiModel;
import com.ibanity.apis.client.jsonapi.RelationshipsApiModel;
import com.ibanity.apis.client.mappers.IbanityModelMapper;
import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.products.ponto_connect.mappers.SynchronizationMapper;
import com.ibanity.apis.client.products.ponto_connect.models.Account;
import com.ibanity.apis.client.products.ponto_connect.models.Synchronization;
import com.ibanity.apis.client.products.ponto_connect.models.read.AccountReadQuery;
import com.ibanity.apis.client.products.ponto_connect.models.read.AccountsReadQuery;
import com.ibanity.apis.client.products.ponto_connect.services.AccountService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;

import java.util.function.Function;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.mapResource;
import static com.ibanity.apis.client.mappers.IbanityModelMapper.toIbanityModel;
import static com.ibanity.apis.client.paging.IbanityPagingSpec.DEFAULT_PAGING_SPEC;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;

public class AccountServiceImpl implements AccountService {

    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public AccountServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public Account find(AccountReadQuery accountReadQuery) {
        String uri = getUrl()
                + "/"
                + accountReadQuery.getAccountId();
        HttpResponse response = ibanityHttpClient.get(buildUri(uri), accountReadQuery.getAdditionalHeaders(), accountReadQuery.getAccessToken());
        return mapResource(response, customMappingFunction());
    }

    @Override
    public IbanityCollection<Account> list(AccountsReadQuery accountsReadQuery) {
        IbanityPagingSpec pagingSpec = accountsReadQuery.getPagingSpec();

        if (pagingSpec == null) {
            pagingSpec = DEFAULT_PAGING_SPEC;
        }

        HttpResponse response = ibanityHttpClient.get(buildUri(getUrl(), pagingSpec), accountsReadQuery.getAdditionalHeaders(), accountsReadQuery.getAccessToken());
        return IbanityModelMapper.mapCollection(response, customMappingFunction());
    }

    private String getUrl() {
        String url = apiUrlProvider.find(IbanityProduct.PontoConnect, "accounts")
                .replace(Account.API_URL_TAG_ID, "");

        return StringUtils.removeEnd(url, "/");
    }

    private Function<DataApiModel, Account> customMappingFunction() {
        return dataApiModel -> {
            Account account = toIbanityModel(dataApiModel, Account.class);
            if (dataApiModel.getMeta() != null) {
                Synchronization synchronization = SynchronizationMapper.map(dataApiModel.getMeta().getLatestSynchronization());
                account.setLatestSynchronization(synchronization);
                account.setSynchronizedAt(dataApiModel.getMeta().getSynchronizedAt());
            }

            RelationshipsApiModel financialInstitution = dataApiModel.getRelationships().get("financialInstitution");
            if (financialInstitution != null) {
                account.setFinancialInstitutionId(financialInstitution.getData().getId());
            }

            return account;
        };
    }
}
