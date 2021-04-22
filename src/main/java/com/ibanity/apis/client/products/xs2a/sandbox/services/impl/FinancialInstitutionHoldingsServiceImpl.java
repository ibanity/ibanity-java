package com.ibanity.apis.client.products.xs2a.sandbox.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.DataApiModel;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.xs2a.models.FinancialInstitution;
import com.ibanity.apis.client.products.xs2a.sandbox.models.FinancialInstitutionAccount;
import com.ibanity.apis.client.products.xs2a.sandbox.models.FinancialInstitutionHolding;
import com.ibanity.apis.client.products.xs2a.sandbox.models.FinancialInstitutionUser;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.create.FinancialInstitutionHoldingCreationQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.delete.FinancialInstitutionHoldingDeleteQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.read.FinancialInstitutionHoldingReadQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.read.FinancialInstitutionHoldingsReadQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.services.FinancialInstitutionHoldingsService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.apache.http.HttpResponse;

import java.util.UUID;
import java.util.function.Function;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.*;
import static com.ibanity.apis.client.mappers.ModelMapperHelper.buildRequest;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;
import static org.apache.commons.lang3.StringUtils.removeEnd;

public class FinancialInstitutionHoldingsServiceImpl implements FinancialInstitutionHoldingsService {

    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public FinancialInstitutionHoldingsServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public FinancialInstitutionHolding find(FinancialInstitutionHoldingReadQuery readQuery) {
        String url =
                getUrl(readQuery.getFinancialInstitutionId().toString(),
                        readQuery.getFinancialInstitutionUserId().toString(),
                        readQuery.getFinancialInstitutionAccountId().toString(),
                        readQuery.getFinancialInstitutionHoldingId().toString());

        HttpResponse response = ibanityHttpClient.get(buildUri(url));
        return mapResource(response, responseMapping());
    }

    @Override
    public IbanityCollection<FinancialInstitutionHolding> list(FinancialInstitutionHoldingsReadQuery readQuery) {
        String url =
                getUrl(readQuery.getFinancialInstitutionId().toString(),
                        readQuery.getFinancialInstitutionUserId().toString(),
                        readQuery.getFinancialInstitutionAccountId().toString(),
                        "");

        HttpResponse response = ibanityHttpClient.get(buildUri(url, readQuery.getPagingSpec()));
        return mapCollection(response, responseMapping());
    }

    @Override
    public FinancialInstitutionHolding delete(FinancialInstitutionHoldingDeleteQuery deleteQuery) {
        String url =
                getUrl(deleteQuery.getFinancialInstitutionId().toString(),
                        deleteQuery.getFinancialInstitutionUserId().toString(),
                        deleteQuery.getFinancialInstitutionAccountId().toString(),
                        deleteQuery.getFinancialInstitutionHoldingId().toString());

        HttpResponse response = ibanityHttpClient.delete(buildUri(url));
        return mapResource(response, FinancialInstitutionHolding.class);
    }

    @Override
    public FinancialInstitutionHolding create(FinancialInstitutionHoldingCreationQuery creationQuery) {
        FinancialInstitutionHolding transaction = requestMapping(creationQuery);
        String url =
                getUrl(creationQuery.getFinancialInstitutionId().toString(),
                        creationQuery.getFinancialInstitutionUserId().toString(),
                        creationQuery.getFinancialInstitutionAccountId().toString(),
                        "");

        RequestApiModel request = buildRequest(FinancialInstitutionHolding.RESOURCE_TYPE, transaction);
        HttpResponse response = ibanityHttpClient.post(buildUri(url), request);
        return mapResource(response, responseMapping());
    }

    private String getUrl(
            String financialInstitutionId,
            String financialInstitutionUserId,
            String financialInstitutionAccountId,
            String financialInstitutionHoldingId) {
        return removeEnd(apiUrlProvider.find(IbanityProduct.Xs2a, "sandbox", "financialInstitution", "financialInstitutionAccount", "financialInstitutionHoldings")
                .replace(FinancialInstitution.API_URL_TAG_ID, financialInstitutionId)
                .replace(FinancialInstitutionUser.API_URL_TAG_ID, financialInstitutionUserId)
                .replace(FinancialInstitutionAccount.API_URL_TAG_ID, financialInstitutionAccountId)
                .replace(FinancialInstitutionHolding.API_URL_TAG_ID, financialInstitutionHoldingId), "/");
    }

    private FinancialInstitutionHolding requestMapping(FinancialInstitutionHoldingCreationQuery holdingCreationQuery) {
        return FinancialInstitutionHolding.builder()
                .totalValuationCurrency(holdingCreationQuery.getTotalValuationCurrency())
                .totalValuation(holdingCreationQuery.getTotalValuation())
                .subtype(holdingCreationQuery.getSubtype())
                .referenceType(holdingCreationQuery.getReferenceType())
                .reference(holdingCreationQuery.getReference())
                .quantity(holdingCreationQuery.getQuantity())
                .name(holdingCreationQuery.getName())
                .lastValuationDate(holdingCreationQuery.getLastValuationDate())
                .lastValuationCurrency(holdingCreationQuery.getLastValuationCurrency())
                .lastValuation(holdingCreationQuery.getLastValuation())
                .build();
    }

    private Function<DataApiModel, FinancialInstitutionHolding> responseMapping() {
        return dataApiModel -> {
            FinancialInstitutionHolding financialInstitutionHolding = toIbanityModel(dataApiModel, FinancialInstitutionHolding.class);
            String accountId = dataApiModel.getRelationships().get("financialInstitutionAccount").getData().getId();
            financialInstitutionHolding.setFinancialInstitutionAccountId(UUID.fromString(accountId));
            return financialInstitutionHolding;
        };
    }
}
