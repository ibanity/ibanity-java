package com.ibanity.apis.client.products.xs2a.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.products.xs2a.models.Synchronization;
import com.ibanity.apis.client.products.xs2a.models.Transaction;
import com.ibanity.apis.client.products.xs2a.models.read.UpdatedTransactionsReadQuery;
import com.ibanity.apis.client.products.xs2a.services.UpdatedTransactionsService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;

import java.util.UUID;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.mapCollection;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;

public class UpdatedTransactionsServiceImpl implements UpdatedTransactionsService {

    private final IbanityHttpClient ibanityHttpClient;
    private final ApiUrlProvider apiUrlProvider;

    public UpdatedTransactionsServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public IbanityCollection<Transaction> list(UpdatedTransactionsReadQuery updatedTransactionsReadQuery) {
        IbanityPagingSpec pagingSpec = updatedTransactionsReadQuery.getPagingSpec();
        if (pagingSpec == null) {
            pagingSpec = IbanityPagingSpec.DEFAULT_PAGING_SPEC;
        }

        String url = getUrl(updatedTransactionsReadQuery.getSynchronizationId());
        HttpResponse response = ibanityHttpClient.get(buildUri(url, pagingSpec), updatedTransactionsReadQuery.getAdditionalHeaders(), updatedTransactionsReadQuery.getCustomerAccessToken());

        return mapCollection(response, Transaction.class);
    }

    private String getUrl(UUID synchronizationId) {
        String url = apiUrlProvider.find(IbanityProduct.Xs2a, "customer", "synchronizations", "updatedTransactions");
        return StringUtils.removeEnd(url
                        .replace(Synchronization.API_URL_TAG_ID, synchronizationId.toString()),
                "/");
    }
}
