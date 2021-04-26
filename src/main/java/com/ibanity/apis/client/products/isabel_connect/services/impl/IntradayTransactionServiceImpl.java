package com.ibanity.apis.client.products.isabel_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.mappers.IsabelModelMapper;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.models.IsabelCollection;
import com.ibanity.apis.client.products.isabel_connect.models.IntradayTransaction;
import com.ibanity.apis.client.products.isabel_connect.models.read.IntradayTransactionsReadQuery;
import com.ibanity.apis.client.products.isabel_connect.models.read.IsabelPagingSpec;
import com.ibanity.apis.client.products.isabel_connect.services.IntradayTransactionService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;

import static com.ibanity.apis.client.utils.URIHelper.buildUri;

public class IntradayTransactionServiceImpl implements IntradayTransactionService {
    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public IntradayTransactionServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public IsabelCollection<IntradayTransaction> list(IntradayTransactionsReadQuery query) {
        IsabelPagingSpec pagingSpec = query.getPagingSpec();

        HttpResponse response = ibanityHttpClient.get(
                buildUri(getUrl(query.getAccountId()), pagingSpec),
                query.getAdditionalHeaders(),
                query.getAccessToken());

        return IsabelModelMapper.mapCollection(response, IntradayTransaction.class);
    }

    private String getUrl(String accountId) {
        String url = apiUrlProvider
                .find(IbanityProduct.IsabelConnect, "account", "intradayTransactions")
                .replace(IntradayTransaction.API_URL_TAG_ID, accountId);

        return StringUtils.removeEnd(url, "/");
    }
}
