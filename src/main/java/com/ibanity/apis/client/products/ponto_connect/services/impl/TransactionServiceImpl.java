package com.ibanity.apis.client.products.ponto_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.ponto_connect.models.Account;
import com.ibanity.apis.client.products.ponto_connect.models.Transaction;
import com.ibanity.apis.client.products.ponto_connect.models.read.TransactionReadQuery;
import com.ibanity.apis.client.products.ponto_connect.models.read.TransactionsReadQuery;
import com.ibanity.apis.client.products.ponto_connect.services.TransactionService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.util.UUID;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.mapCollection;
import static com.ibanity.apis.client.mappers.IbanityModelMapper.mapResource;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;

public class TransactionServiceImpl implements TransactionService {

    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public TransactionServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public Transaction find(TransactionReadQuery transactionReadQuery) {
        URI uri = buildUri(getUrl(transactionReadQuery.getAccountId())
                + "/"
                + transactionReadQuery.getTransactionId());
        String response = ibanityHttpClient.get(uri, transactionReadQuery.getAdditionalHeaders(), transactionReadQuery.getAccessToken());
        return mapResource(response, Transaction.class);
    }

    @Override
    public IbanityCollection<Transaction> list(TransactionsReadQuery transactionsReadQuery) {
        URI uri = buildUri(getUrl(transactionsReadQuery.getAccountId()));
        String response = ibanityHttpClient.get(uri, transactionsReadQuery.getAdditionalHeaders(), transactionsReadQuery.getAccessToken());
        return mapCollection(response, Transaction.class);
    }

    private String getUrl(UUID accountId) {
        String url = apiUrlProvider
                .find(IbanityProduct.PontoConnect, "account", "transactions")
                .replace(Account.API_URL_TAG_ID, accountId.toString())
                .replace(Transaction.API_URL_TAG_ID, "");
        return StringUtils.removeEnd(url, "/");
    }
}
