package com.ibanity.apis.client.products.xs2a.services.impl;

import com.ibanity.apis.client.jsonapi.DataApiModel;
import com.ibanity.apis.client.jsonapi.RelationshipsApiModel;
import com.ibanity.apis.client.mappers.IbanityModelMapper;
import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.network.http.client.IbanityHttpClient;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.products.xs2a.models.Account;
import com.ibanity.apis.client.products.xs2a.models.AccountInformationAccessRequest;
import com.ibanity.apis.client.products.xs2a.models.FinancialInstitution;
import com.ibanity.apis.client.products.xs2a.models.Synchronization;
import com.ibanity.apis.client.products.xs2a.models.factory.read.AccountReadQuery;
import com.ibanity.apis.client.products.xs2a.models.factory.read.AccountsReadQuery;
import com.ibanity.apis.client.products.xs2a.services.AccountsService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import com.ibanity.apis.client.utils.URIHelper;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;
import java.util.function.Function;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.toIbanityModel;
import static com.ibanity.apis.client.paging.IbanityPagingSpec.DEFAULT_PAGING_SPEC;

public class AccountsServiceImpl implements AccountsService {

    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public AccountsServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public Account find(final AccountReadQuery accountReadQuery) {
        try {
            String url = getUrl(accountReadQuery.getFinancialInstitutionId(), null)
                    + "/"
                    + accountReadQuery.getAccountId();
            String response = ibanityHttpClient.get(new URI(url), accountReadQuery.getCustomerAccessToken());
            return IbanityModelMapper.mapResource(response, customMappingFunction());
        } catch (
                URISyntaxException e) {
            throw new IllegalStateException("URL cannot be build", e);
        }
    }

    @Override
    public IbanityCollection<Account> list(final AccountsReadQuery accountsReadQuery) {
        IbanityPagingSpec pagingSpec = accountsReadQuery.getPagingSpec();

        if (pagingSpec == null) {
            pagingSpec = DEFAULT_PAGING_SPEC;
        }

        String url = getUrl(accountsReadQuery.getFinancialInstitutionId(), accountsReadQuery.getAccountInformationAccessRequestId());
        String response = ibanityHttpClient.get(URIHelper.buildUri(url, pagingSpec), accountsReadQuery.getCustomerAccessToken());
        return IbanityModelMapper.mapCollection(response, customMappingFunction());
    }

    private Function<DataApiModel, Account> customMappingFunction() {
        return dataApiModel -> {
            Account account = toIbanityModel(dataApiModel, Account.class);
            Synchronization synchronization = toIbanityModel(dataApiModel.getMeta().getLatestSynchronization(), Synchronization.class);
            RelationshipsApiModel financialInstitution = dataApiModel.getRelationships().get("financialInstitution");

            account.setFinancialInstitutionId(financialInstitution.getData().getId());
            account.setSynchronizedAt(dataApiModel.getMeta().getSynchronizedAt());
            account.setLastSynchronization(synchronization);
            return account;
        };
    }

    private String getUrl(UUID financialInstitutionId, UUID accountInformationAccessRequestId) {
        String url;
        if (accountInformationAccessRequestId != null && financialInstitutionId != null) {
            url = apiUrlProvider.find(IbanityProduct.Xs2a, "customer", "financialInstitution", "accountInformationAccessRequest", "accounts")
                    .replace(FinancialInstitution.API_URL_TAG_ID, financialInstitutionId.toString())
                    .replace(AccountInformationAccessRequest.API_URL_TAG_ID, accountInformationAccessRequestId.toString())
                    .replace(Account.API_URL_TAG_ID, "");
        } else if (financialInstitutionId != null) {
            url = apiUrlProvider.find(IbanityProduct.Xs2a, "customer", "financialInstitution", "accounts")
                    .replace(FinancialInstitution.API_URL_TAG_ID, financialInstitutionId.toString())
                    .replace(Account.API_URL_TAG_ID, "");
        } else {
            url = apiUrlProvider.find(IbanityProduct.Xs2a, "customer", "accounts")
                    .replace(Account.API_URL_TAG_ID, "");
        }

        return StringUtils.removeEnd(url, "/");
    }
}
