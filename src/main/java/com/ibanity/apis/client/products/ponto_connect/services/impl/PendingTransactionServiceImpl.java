package com.ibanity.apis.client.products.ponto_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.products.ponto_connect.models.Account;
import com.ibanity.apis.client.products.ponto_connect.models.PendingTransaction;
import com.ibanity.apis.client.products.ponto_connect.models.Synchronization;
import com.ibanity.apis.client.products.ponto_connect.models.read.PendingTransactionReadQuery;
import com.ibanity.apis.client.products.ponto_connect.models.read.PendingTransactionsReadQuery;
import com.ibanity.apis.client.products.ponto_connect.services.PendingTransactionService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;

import java.net.URI;
import java.util.UUID;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.mapCollection;
import static com.ibanity.apis.client.mappers.IbanityModelMapper.mapResource;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;

public class PendingTransactionServiceImpl implements PendingTransactionService {

    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public PendingTransactionServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public PendingTransaction find(PendingTransactionReadQuery pendingTransactionReadQuery) {
        URI uri = buildUri(getUrl(pendingTransactionReadQuery.getAccountId())
                + "/"
                + pendingTransactionReadQuery.getPendingTransactionId());
        HttpResponse response = ibanityHttpClient.get(uri, pendingTransactionReadQuery.getAdditionalHeaders(), pendingTransactionReadQuery.getAccessToken());
        return mapResource(response, PendingTransaction.class);
    }

    @Override
    public IbanityCollection<PendingTransaction> list(PendingTransactionsReadQuery pendingTransactionsReadQuery) {
        IbanityPagingSpec pagingSpec = pendingTransactionsReadQuery.getPagingSpec();
        if (pagingSpec == null) {
            pagingSpec = IbanityPagingSpec.DEFAULT_PAGING_SPEC;
        }

        URI uri = buildUri(getUrl(pendingTransactionsReadQuery.getAccountId()), pagingSpec);

        HttpResponse response = ibanityHttpClient.get(uri, pendingTransactionsReadQuery.getAdditionalHeaders(), pendingTransactionsReadQuery.getAccessToken());
        return mapCollection(response, PendingTransaction.class);
    }

    @Override
    public IbanityCollection<PendingTransaction> listUpdatedForSynchronization(PendingTransactionsReadQuery pendingTransactionsReadQuery) {
        IbanityPagingSpec pagingSpec = pendingTransactionsReadQuery.getPagingSpec();
        if (pagingSpec == null) {
            pagingSpec = IbanityPagingSpec.DEFAULT_PAGING_SPEC;
        }

        URI uri = buildUri(getUrlForSynchronizationId(pendingTransactionsReadQuery.getSynchronizationId()), pagingSpec);

        HttpResponse response = ibanityHttpClient.get(uri, pendingTransactionsReadQuery.getAdditionalHeaders(), pendingTransactionsReadQuery.getAccessToken());
        return mapCollection(response, PendingTransaction.class);
    }

    private String getUrl(UUID accountId) {
        String url = apiUrlProvider
                .find(IbanityProduct.PontoConnect, "account", "pendingTransactions")
                .replace(Account.API_URL_TAG_ID, accountId.toString())
                .replace(PendingTransaction.API_URL_TAG_ID, "");
        return StringUtils.removeEnd(url, "/");
    }

    private String getUrlForSynchronizationId(UUID synchronizationId) {
        String url = apiUrlProvider
                .find(IbanityProduct.PontoConnect, "synchronization", "updatedPendingTransactions")
                .replace(Synchronization.API_URL_TAG_ID, synchronizationId.toString());
        return StringUtils.removeEnd(url, "/");
    }
}
