package com.ibanity.apis.client.products.xs2a.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.products.xs2a.models.Account;
import com.ibanity.apis.client.products.xs2a.models.FinancialInstitution;
import com.ibanity.apis.client.products.xs2a.models.PendingTransaction;
import com.ibanity.apis.client.products.xs2a.models.Synchronization;
import com.ibanity.apis.client.products.xs2a.models.read.PendingTransactionReadQuery;
import com.ibanity.apis.client.products.xs2a.models.read.PendingTransactionsReadQuery;
import com.ibanity.apis.client.products.xs2a.services.PendingTransactionsService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;

import java.util.UUID;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.mapCollection;
import static com.ibanity.apis.client.mappers.IbanityModelMapper.mapResource;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;

public class PendingTransactionsServiceImpl implements PendingTransactionsService {

    private final IbanityHttpClient ibanityHttpClient;
    private final ApiUrlProvider apiUrlProvider;

    public PendingTransactionsServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public IbanityCollection<PendingTransaction> list(PendingTransactionsReadQuery pendingTransactionsReadQuery) {
        IbanityPagingSpec pagingSpec = pendingTransactionsReadQuery.getPagingSpec();
        if (pagingSpec == null) {
            pagingSpec = IbanityPagingSpec.DEFAULT_PAGING_SPEC;
        }

        String url = getUrl(pendingTransactionsReadQuery.getFinancialInstitutionId(), pendingTransactionsReadQuery.getAccountId());
        HttpResponse response = ibanityHttpClient.get(buildUri(url, pagingSpec), pendingTransactionsReadQuery.getAdditionalHeaders(), pendingTransactionsReadQuery.getCustomerAccessToken());

        return mapCollection(response, PendingTransaction.class);
    }

    @Override
    public IbanityCollection<PendingTransaction> listUpdatedForSynchronization(PendingTransactionsReadQuery pendingTransactionsReadQuery) {
        IbanityPagingSpec pagingSpec = pendingTransactionsReadQuery.getPagingSpec();
        if (pagingSpec == null) {
            pagingSpec = IbanityPagingSpec.DEFAULT_PAGING_SPEC;
        }

        String url = getUrlForSynchronizationId(pendingTransactionsReadQuery.getSynchronizationId());
        HttpResponse response = ibanityHttpClient.get(buildUri(url, pagingSpec), pendingTransactionsReadQuery.getAdditionalHeaders(), pendingTransactionsReadQuery.getCustomerAccessToken());

        return mapCollection(response, PendingTransaction.class);
    }

    @Override
    public PendingTransaction find(PendingTransactionReadQuery pendingTransactionReadQuery) {
        String url =
                getUrl(pendingTransactionReadQuery.getFinancialInstitutionId(), pendingTransactionReadQuery.getAccountId())
                        + "/"
                        + pendingTransactionReadQuery.getPendingTransactionId().toString();
        HttpResponse response = ibanityHttpClient.get(buildUri(url), pendingTransactionReadQuery.getAdditionalHeaders(), pendingTransactionReadQuery.getCustomerAccessToken());
        return mapResource(response, PendingTransaction.class);
    }

    private String getUrl(UUID financialInstitutionId, UUID accountId) {
        String url = apiUrlProvider
            .find(IbanityProduct.Xs2a, "customer", "financialInstitution", "account", "pendingTransactions")
            .replace(FinancialInstitution.API_URL_TAG_ID, financialInstitutionId.toString())
            .replace(Account.API_URL_TAG_ID, accountId.toString())
            .replace(PendingTransaction.API_URL_TAG_ID, "");
        return StringUtils.removeEnd(url, "/");
    }

    private String getUrlForSynchronizationId(UUID synchronizationId) {
        String url = apiUrlProvider
            .find(IbanityProduct.Xs2a, "customer", "synchronization", "updatedPendingTransactions")
            .replace(Synchronization.API_URL_TAG_ID, synchronizationId.toString());
        return StringUtils.removeEnd(url, "/");
    }
}
