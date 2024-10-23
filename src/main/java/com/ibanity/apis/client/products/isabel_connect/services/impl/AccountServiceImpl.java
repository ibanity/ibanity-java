package com.ibanity.apis.client.products.isabel_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.isabel_connect.DataApiModel;
import com.ibanity.apis.client.mappers.IsabelModelMapper;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.models.IsabelCollection;
import com.ibanity.apis.client.products.isabel_connect.models.Account;
import com.ibanity.apis.client.products.isabel_connect.models.read.AccountReadQuery;
import com.ibanity.apis.client.products.isabel_connect.models.read.AccountsReadQuery;
import com.ibanity.apis.client.products.isabel_connect.models.read.IsabelPagingSpec;
import com.ibanity.apis.client.products.isabel_connect.services.AccountService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;

import java.util.function.Function;

import static com.ibanity.apis.client.mappers.IsabelModelMapper.toIsabelModel;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;

public class AccountServiceImpl implements AccountService {

    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public AccountServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public Account find(AccountReadQuery query) {
        String url = getUrl(query.getAccountId());
        HttpResponse response = ibanityHttpClient.get(buildUri(url), query.getAdditionalHeaders(), query.getAccessToken());
        return IsabelModelMapper.mapResource(response, Account.class);
    }

    @Override
    public IsabelCollection<Account> list(AccountsReadQuery query) {
        IsabelPagingSpec pagingSpec = query.getPagingSpec();

        if (pagingSpec == null) {
            pagingSpec = IsabelPagingSpec.DEFAULT_PAGING_SPEC;
        }

        HttpResponse response = ibanityHttpClient.get(buildUri(getUrl(), pagingSpec), query.getAdditionalHeaders(), query.getAccessToken());
        return IsabelModelMapper.mapCollection(response, customMappingFunction());
    }

    private String getUrl() {
        return getUrl("");
    }

    private String getUrl(String accountId) {
        String url = apiUrlProvider
                .find(IbanityProduct.IsabelConnect, "accounts")
                .replace(Account.API_URL_TAG_ID, accountId);

        return StringUtils.removeEnd(url, "/");
    }

    private Function<DataApiModel, Account> customMappingFunction() {
        return dataApiModel -> toIsabelModel(dataApiModel, Account.class);
    }
}
