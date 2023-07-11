package com.ibanity.apis.client.products.xs2a.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.products.xs2a.models.Account;
import com.ibanity.apis.client.products.xs2a.models.FinancialInstitution;
import com.ibanity.apis.client.products.xs2a.models.Synchronization;
import com.ibanity.apis.client.products.xs2a.models.Transaction;
import com.ibanity.apis.client.products.xs2a.models.read.TransactionReadQuery;
import com.ibanity.apis.client.products.xs2a.models.read.TransactionsReadQuery;
import com.ibanity.apis.client.products.xs2a.services.TransactionsService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;

import java.util.UUID;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.mapCollection;
import static com.ibanity.apis.client.mappers.IbanityModelMapper.mapResource;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;

public class TransactionsServiceImpl implements TransactionsService {

    private final IbanityHttpClient ibanityHttpClient;
    private final ApiUrlProvider apiUrlProvider;

    public TransactionsServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public IbanityCollection<Transaction> list(TransactionsReadQuery transactionsReadQuery) {
        IbanityPagingSpec pagingSpec = transactionsReadQuery.getPagingSpec();
        if (pagingSpec == null) {
            pagingSpec = IbanityPagingSpec.DEFAULT_PAGING_SPEC;
        }

        String url = getUrl(transactionsReadQuery.getFinancialInstitutionId(), transactionsReadQuery.getAccountId());
        HttpResponse response = ibanityHttpClient.get(buildUri(url, pagingSpec), transactionsReadQuery.getAdditionalHeaders(), transactionsReadQuery.getCustomerAccessToken());

        return mapCollection(response, Transaction.class);
    }

    @Override
    public IbanityCollection<Transaction> listUpdatedForSynchronization(TransactionsReadQuery transactionsReadQuery) {
        IbanityPagingSpec pagingSpec = transactionsReadQuery.getPagingSpec();
        if (pagingSpec == null) {
            pagingSpec = IbanityPagingSpec.DEFAULT_PAGING_SPEC;
        }

        String url = getUrlForSynchronizationId(transactionsReadQuery.getSynchronizationId());
        HttpResponse response = ibanityHttpClient.get(buildUri(url, pagingSpec), transactionsReadQuery.getAdditionalHeaders(), transactionsReadQuery.getCustomerAccessToken());

        return mapCollection(response, Transaction.class);
    }

    @Override
    public Transaction find(TransactionReadQuery transactionReadQuery) {
        String url =
                getUrl(transactionReadQuery.getFinancialInstitutionId(), transactionReadQuery.getAccountId())
                        + "/"
                        + transactionReadQuery.getTransactionId().toString();
        HttpResponse response = ibanityHttpClient.get(buildUri(url), transactionReadQuery.getAdditionalHeaders(), transactionReadQuery.getCustomerAccessToken());
        return mapResource(response, Transaction.class);
    }

    private String getUrl(UUID financialInstitutionId, UUID accountId) {
        String url = apiUrlProvider
            .find(IbanityProduct.Xs2a, "customer", "financialInstitution", "account", "transactions")
            .replace(FinancialInstitution.API_URL_TAG_ID, financialInstitutionId.toString())
            .replace(Account.API_URL_TAG_ID, accountId.toString())
            .replace(Transaction.API_URL_TAG_ID, "");
        return StringUtils.removeEnd(url, "/");
    }

    private String getUrlForSynchronizationId(UUID synchronizationId) {
        String url = apiUrlProvider
            .find(IbanityProduct.Xs2a, "customer", "synchronization", "updatedTransactions")
            .replace(Synchronization.API_URL_TAG_ID, synchronizationId.toString());
        return StringUtils.removeEnd(url, "/");
    }
}
