package com.ibanity.apis.client.products.ponto_connect.sandbox.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.products.ponto_connect.sandbox.models.FinancialInstitutionAccount;
import com.ibanity.apis.client.products.ponto_connect.sandbox.models.FinancialInstitutionTransaction;
import com.ibanity.apis.client.products.ponto_connect.sandbox.models.factory.create.FinancialInstitutionTransactionCreationQuery;
import com.ibanity.apis.client.products.ponto_connect.sandbox.models.factory.read.FinancialInstitutionTransactionReadQuery;
import com.ibanity.apis.client.products.ponto_connect.sandbox.models.factory.read.FinancialInstitutionTransactionsReadQuery;
import com.ibanity.apis.client.products.ponto_connect.sandbox.models.factory.update.FinancialInstitutionTransactionUpdateQuery;
import com.ibanity.apis.client.products.ponto_connect.sandbox.services.FinancialInstitutionTransactionsService;
import com.ibanity.apis.client.products.xs2a.models.FinancialInstitution;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.apache.http.HttpResponse;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.mapCollection;
import static com.ibanity.apis.client.mappers.IbanityModelMapper.mapResource;
import static com.ibanity.apis.client.mappers.ModelMapperHelper.buildRequest;
import static com.ibanity.apis.client.models.IbanityProduct.PontoConnect;
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
                        readQuery.getFinancialInstitutionAccountId().toString(),
                        readQuery.getFinancialInstitutionTransactionId().toString());

        HttpResponse response = ibanityHttpClient.get(buildUri(url), readQuery.getAccessToken());
        return mapResource(response, FinancialInstitutionTransaction.class);
    }

    @Override
    public IbanityCollection<FinancialInstitutionTransaction> list(FinancialInstitutionTransactionsReadQuery readQuery) {
        String url =
                getUrl(readQuery.getFinancialInstitutionId().toString(),
                        readQuery.getFinancialInstitutionAccountId().toString(),
                        "");

        HttpResponse response = ibanityHttpClient.get(buildUri(url, readQuery.getPagingSpec()), readQuery.getAccessToken());
        return mapCollection(response, FinancialInstitutionTransaction.class);
    }

    @Override
    public FinancialInstitutionTransaction create(FinancialInstitutionTransactionCreationQuery creationQuery) {
        FinancialInstitutionTransaction transaction = requestMapping(creationQuery);
        String url =
                getUrl(creationQuery.getFinancialInstitutionId().toString(),
                        creationQuery.getFinancialInstitutionAccountId().toString(),
                        "");

        RequestApiModel request = buildRequest(FinancialInstitutionTransaction.RESOURCE_TYPE, transaction);
        HttpResponse response = ibanityHttpClient.post(buildUri(url), request, creationQuery.getAccessToken());
        return mapResource(response, FinancialInstitutionTransaction.class);
    }

    @Override
    public FinancialInstitutionTransaction update(FinancialInstitutionTransactionUpdateQuery transactionUpdateQuery) {
        FinancialInstitutionTransaction transaction = requestMapping(transactionUpdateQuery);
        String url =
                getUrl(transactionUpdateQuery.getFinancialInstitutionId().toString(),
                        transactionUpdateQuery.getFinancialInstitutionAccountId().toString(),
                        transactionUpdateQuery.getFinancialInstitutionTransactionId().toString());

        RequestApiModel request = buildRequest(FinancialInstitutionTransaction.RESOURCE_TYPE, transaction);
        HttpResponse response = ibanityHttpClient.patch(buildUri(url), request, transactionUpdateQuery.getAccessToken());
        return mapResource(response, FinancialInstitutionTransaction.class);
    }

    private String getUrl(
            String financialInstitutionId,
            String financialInstitutionAccountId,
            String financialInstitutionTransactionId) {
        return removeEnd(apiUrlProvider.find(PontoConnect, "sandbox", "financialInstitution", "financialInstitutionAccount", "financialInstitutionTransactions")
                .replace(FinancialInstitution.API_URL_TAG_ID, financialInstitutionId)
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
                .bankTransactionCode(transactionCreationQuery.getBankTransactionCode())
                .proprietaryBankTransactionCode(transactionCreationQuery.getProprietaryBankTransactionCode())
                .additionalInformation(transactionCreationQuery.getAdditionalInformation())
                .creditorId(transactionCreationQuery.getCreditorId())
                .mandateId(transactionCreationQuery.getMandateId())
                .purposeCode(transactionCreationQuery.getPurposeCode())
                .endToEndId(transactionCreationQuery.getEndToEndId())
                .build();
    }

    private FinancialInstitutionTransaction requestMapping(FinancialInstitutionTransactionUpdateQuery transactionUpdateQuery) {
        return FinancialInstitutionTransaction.builder()
                .remittanceInformation(transactionUpdateQuery.getRemittanceInformation())
                .counterpartName(transactionUpdateQuery.getCounterpartName())
                .description(transactionUpdateQuery.getDescription())
                .bankTransactionCode(transactionUpdateQuery.getBankTransactionCode())
                .proprietaryBankTransactionCode(transactionUpdateQuery.getProprietaryBankTransactionCode())
                .additionalInformation(transactionUpdateQuery.getAdditionalInformation())
                .creditorId(transactionUpdateQuery.getCreditorId())
                .mandateId(transactionUpdateQuery.getMandateId())
                .purposeCode(transactionUpdateQuery.getPurposeCode())
                .endToEndId(transactionUpdateQuery.getEndToEndId())
                .build();
    }
}
