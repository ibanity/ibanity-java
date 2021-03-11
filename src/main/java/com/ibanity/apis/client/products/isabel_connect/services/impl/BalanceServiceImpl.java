package com.ibanity.apis.client.products.isabel_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.isabel_connect.DataApiModel;
import com.ibanity.apis.client.mappers.IsabelModelMapper;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.models.IsabelCollection;
import com.ibanity.apis.client.products.isabel_connect.models.Account;
import com.ibanity.apis.client.products.isabel_connect.models.Balance;
import com.ibanity.apis.client.products.isabel_connect.models.read.BalanceReadQuery;
import com.ibanity.apis.client.products.isabel_connect.models.read.IsabelPagingSpec;
import com.ibanity.apis.client.products.isabel_connect.services.BalanceService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;

import java.net.URI;
import java.util.function.Function;

import static com.ibanity.apis.client.mappers.IsabelModelMapper.toIsabelModel;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;

public class BalanceServiceImpl implements BalanceService {

    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public BalanceServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public IsabelCollection<Balance> list(BalanceReadQuery query) {
        IsabelPagingSpec pagingSpec = query.getPagingSpec();

        if (pagingSpec == null) {
            pagingSpec = IsabelPagingSpec.DEFAULT_PAGING_SPEC;
        }

        URI uri = buildUri(getUrl(query.getAccountId()), pagingSpec);
        HttpResponse response = ibanityHttpClient.get(uri, query.getAdditionalHeaders(), query.getAccessToken());
        return IsabelModelMapper.mapCollection(response, customMappingFunction());
    }

    private String getUrl(String accountId) {
        String url = apiUrlProvider
                .find(IbanityProduct.IsabelConnect, "accounts", "balances")
                .replace(Account.API_URL_TAG_ID, accountId);

        return StringUtils.removeEnd(url, "/");
    }

    private Function<DataApiModel, Balance> customMappingFunction() {
        return dataApiModel -> toIsabelModel(dataApiModel, Balance.class);
    }
}
