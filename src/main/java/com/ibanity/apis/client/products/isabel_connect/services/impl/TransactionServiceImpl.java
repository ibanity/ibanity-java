package com.ibanity.apis.client.products.isabel_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.isabel_connect.DataApiModel;
import com.ibanity.apis.client.mappers.IsabelModelMapper;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.models.IsabelCollection;
import com.ibanity.apis.client.products.isabel_connect.models.Transaction;
import com.ibanity.apis.client.products.isabel_connect.models.read.IsabelPagingSpec;
import com.ibanity.apis.client.products.isabel_connect.models.read.TransactionsReadQuery;
import com.ibanity.apis.client.products.isabel_connect.services.TransactionService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;

import java.util.function.Function;

import static com.ibanity.apis.client.mappers.IsabelModelMapper.toIsabelModel;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;

public class TransactionServiceImpl implements TransactionService {
    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public TransactionServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public IsabelCollection<Transaction> list(TransactionsReadQuery query) {
        IsabelPagingSpec pagingSpec = query.getPagingSpec();

        if (pagingSpec == null) {
            pagingSpec = IsabelPagingSpec.DEFAULT_PAGING_SPEC;
        }

        HttpResponse response = ibanityHttpClient.get(
                buildUri(getUrl(query.getAccountId()), pagingSpec),
                query.getAdditionalHeaders(),
                query.getAccessToken());

        return IsabelModelMapper.mapCollection(response, customMappingFunction());
    }

    private String getUrl(String accountId) {
        String url = apiUrlProvider
                .find(IbanityProduct.IsabelConnect, "account", "transactions")
                .replace(Transaction.API_URL_TAG_ID, accountId);

        return StringUtils.removeEnd(url, "/");
    }

    private Function<DataApiModel, Transaction> customMappingFunction() {
        return dataApiModel -> toIsabelModel(dataApiModel, Transaction.class);
    }
}
