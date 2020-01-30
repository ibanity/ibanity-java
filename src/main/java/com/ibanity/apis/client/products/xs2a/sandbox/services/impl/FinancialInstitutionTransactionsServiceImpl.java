package com.ibanity.apis.client.products.xs2a.sandbox.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.DataApiModel;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.xs2a.models.FinancialInstitution;
import com.ibanity.apis.client.products.xs2a.sandbox.models.FinancialInstitutionAccount;
import com.ibanity.apis.client.products.xs2a.sandbox.models.FinancialInstitutionTransaction;
import com.ibanity.apis.client.products.xs2a.sandbox.models.FinancialInstitutionUser;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.create.FinancialInstitutionTransactionCreationQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.delete.FinancialInstitutionTransactionDeleteQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.read.FinancialInstitutionTransactionReadQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.read.FinancialInstitutionTransactionsReadQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.services.FinancialInstitutionTransactionsService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.apache.http.HttpResponse;

import java.util.UUID;
import java.util.function.Function;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.*;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;
import static org.apache.commons.lang3.StringUtils.removeEnd;

public class FinancialInstitutionTransactionsServiceImpl implements FinancialInstitutionTransactionsService {

    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public FinancialInstitutionTransactionsServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public FinancialInstitutionTransaction find(FinancialInstitutionTransactionReadQuery readQuery) {
        String url =
                getUrl(readQuery.getFinancialInstitutionId().toString(),
                        readQuery.getFinancialInstitutionUserId().toString(),
                        readQuery.getFinancialInstitutionAccountId().toString(),
                        readQuery.getFinancialInstitutionTransactionId().toString());

        HttpResponse response = ibanityHttpClient.get(buildUri(url));
        return mapResource(response, responseMapping());
    }

    @Override
    public IbanityCollection<FinancialInstitutionTransaction> list(FinancialInstitutionTransactionsReadQuery readQuery) {
        String url =
                getUrl(readQuery.getFinancialInstitutionId().toString(),
                        readQuery.getFinancialInstitutionUserId().toString(),
                        readQuery.getFinancialInstitutionAccountId().toString(),
                        "");

        HttpResponse response = ibanityHttpClient.get(buildUri(url, readQuery.getPagingSpec()));
        return mapCollection(response, responseMapping());
    }

    @Override
    public FinancialInstitutionTransaction delete(FinancialInstitutionTransactionDeleteQuery deleteQuery) {
        String url =
                getUrl(deleteQuery.getFinancialInstitutionId().toString(),
                        deleteQuery.getFinancialInstitutionUserId().toString(),
                        deleteQuery.getFinancialInstitutionAccountId().toString(),
                        deleteQuery.getFinancialInstitutionTransactionId().toString());

        HttpResponse response = ibanityHttpClient.delete(buildUri(url));
        return mapResource(response, FinancialInstitutionTransaction.class);
    }

    @Override
    public FinancialInstitutionTransaction create(FinancialInstitutionTransactionCreationQuery creationQuery) {
        FinancialInstitutionTransaction transaction = requestMapping(creationQuery);
        String url =
                getUrl(creationQuery.getFinancialInstitutionId().toString(),
                        creationQuery.getFinancialInstitutionUserId().toString(),
                        creationQuery.getFinancialInstitutionAccountId().toString(),
                        "");

        RequestApiModel request = buildRequest(FinancialInstitutionTransaction.RESOURCE_TYPE, transaction);
        HttpResponse response = ibanityHttpClient.post(buildUri(url), request);
        return mapResource(response, responseMapping());
    }

    private String getUrl(
            String financialInstitutionId,
            String financialInstitutionUserId,
            String financialInstitutionAccountId,
            String financialInstitutionTransactionId) {
        return removeEnd(apiUrlProvider.find(IbanityProduct.Xs2a, "sandbox", "financialInstitution", "financialInstitutionAccount", "financialInstitutionTransactions")
                .replace(FinancialInstitution.API_URL_TAG_ID, financialInstitutionId)
                .replace(FinancialInstitutionUser.API_URL_TAG_ID, financialInstitutionUserId)
                .replace(FinancialInstitutionAccount.API_URL_TAG_ID, financialInstitutionAccountId)
                .replace(FinancialInstitutionTransaction.API_URL_TAG_ID, financialInstitutionTransactionId), "/");
    }

    private FinancialInstitutionTransaction requestMapping(FinancialInstitutionTransactionCreationQuery transactionCreationQuery) {
        return FinancialInstitutionTransaction.builder()
                .amount(transactionCreationQuery.getAmount())
                .currency(transactionCreationQuery.getCurrency())
                .remittanceInformation(transactionCreationQuery.getRemittanceInformation())
                .remittanceInformationType(transactionCreationQuery.getRemittanceInformationType())
                .counterpartName(transactionCreationQuery.getCounterpartName())
                .counterpartReference(transactionCreationQuery.getCounterpartReference())
                .valueDate(transactionCreationQuery.getValueDate())
                .executionDate(transactionCreationQuery.getExecutionDate())
                .description(transactionCreationQuery.getDescription())
                .build();
    }

    private Function<DataApiModel, FinancialInstitutionTransaction> responseMapping() {
        return dataApiModel -> {
            FinancialInstitutionTransaction financialInstitutionTransaction = toIbanityModel(dataApiModel, FinancialInstitutionTransaction.class);
            UUID accountId = dataApiModel.getRelationships().get("financialInstitutionAccount").getData().getId();
            financialInstitutionTransaction.setFinancialInstitutionAccountId(accountId);
            return financialInstitutionTransaction;
        };
    }
}
