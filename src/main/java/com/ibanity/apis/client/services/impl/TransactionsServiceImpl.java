package com.ibanity.apis.client.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibanity.apis.client.jsonapi.CollectionApiModel;
import com.ibanity.apis.client.jsonapi.DataApiModel;
import com.ibanity.apis.client.jsonapi.ResourceApiModel;
import com.ibanity.apis.client.models.Account;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.models.Transaction;
import com.ibanity.apis.client.models.factory.read.TransactionReadQuery;
import com.ibanity.apis.client.models.factory.read.TransactionsReadQuery;
import com.ibanity.apis.client.network.http.client.IbanityHttpClient;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.services.ApiUrlProvider;
import com.ibanity.apis.client.services.TransactionsService;
import com.ibanity.apis.client.utils.URIHelper;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;
import java.util.stream.Collectors;

public class TransactionsServiceImpl implements TransactionsService {

    private final IbanityHttpClient ibanityHttpClient;
    private final ObjectMapper objectMapper;
    private final ApiUrlProvider apiUrlProvider;

    public TransactionsServiceImpl(
            IbanityHttpClient ibanityHttpClient,
            ObjectMapper objectMapper,
            ApiUrlProvider apiUrlProvider) {
        this.ibanityHttpClient = ibanityHttpClient;
        this.objectMapper = objectMapper;
        this.apiUrlProvider = apiUrlProvider;
    }

    @Override
    public IbanityCollection<Transaction> list(final TransactionsReadQuery transactionsReadQuery) {
        try {
            IbanityPagingSpec pagingSpec = transactionsReadQuery.getPagingSpec();
            if (pagingSpec == null) {
                pagingSpec = IbanityPagingSpec.DEFAULT_PAGING_SPEC;
            }

            String url = getUrl(transactionsReadQuery.getFinancialInstitutionId(), transactionsReadQuery.getAccountId());
            String response = ibanityHttpClient.get(URIHelper.buildUri(url, pagingSpec), transactionsReadQuery.getCustomerAccessToken());

            CollectionApiModel<Transaction> collectionApiModel = objectMapper.readValue(response, new TypeReference<CollectionApiModel<Transaction>>() {
            });
            return toIbanityCollection(collectionApiModel);
        } catch (URISyntaxException e) {
            throw new IllegalStateException("URL cannot be build", e);
        } catch (IOException e) {
            throw new IllegalArgumentException("Response cannot be parsed", e);
        }
    }

    @Override
    public Transaction find(final TransactionReadQuery transactionReadQuery) {
        try {
            String url =
                    getUrl(transactionReadQuery.getFinancialInstitutionId(), transactionReadQuery.getAccountId())
                            + "/"
                            + transactionReadQuery.getTransactionId().toString();
            String response = ibanityHttpClient.get(new URI(url), transactionReadQuery.getCustomerAccessToken());
            ResourceApiModel<Transaction> transaction = objectMapper.readValue(response, new TypeReference<ResourceApiModel<Transaction>>() {
            });
            return toTransaction(transaction.getData());
        } catch (URISyntaxException e) {
            throw new IllegalStateException("URL cannot be build", e);
        } catch (IOException e) {
            throw new IllegalArgumentException("Response cannot be parsed", e);
        }
    }

    private IbanityCollection<Transaction> toIbanityCollection(CollectionApiModel<Transaction> collectionApiModel) {
        return IbanityCollection.<Transaction>builder()
                .items(collectionApiModel.getData().stream().map(this::toTransaction).collect(Collectors.toList()))
                .build();
    }

    private Transaction toTransaction(DataApiModel<Transaction> transactionData) {
        Transaction transaction = transactionData.getAttributes();
        transaction.setId(transactionData.getId());
        transaction.setSelfLink(transactionData.getLinks().getSelf());
        return transaction;
    }

    private String getUrl(UUID financialInstitutionId, UUID accountId) throws URISyntaxException {
        String url = apiUrlProvider.find("customer", "financialInstitution", "transactions");
        return StringUtils.removeEnd(url
                        .replace(FinancialInstitution.API_URL_TAG_ID, financialInstitutionId.toString())
                        .replace(Account.API_URL_TAG_ID, accountId.toString())
                        .replace(Transaction.API_URL_TAG_ID, ""),
                "/");
    }
}
