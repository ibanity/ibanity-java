package com.ibanity.apis.client.products.ponto_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.DataApiModel;
import com.ibanity.apis.client.jsonapi.RelationshipsApiModel;
import com.ibanity.apis.client.mappers.IbanityModelMapper;
import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.products.ponto_connect.models.IntegrationAccount;
import com.ibanity.apis.client.products.ponto_connect.models.read.IntegrationAccountsReadQuery;
import com.ibanity.apis.client.products.ponto_connect.services.IntegrationAccountService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.apache.http.HttpResponse;

import java.util.UUID;
import java.util.function.Function;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.toIbanityModel;
import static com.ibanity.apis.client.models.IbanityProduct.PontoConnect;
import static com.ibanity.apis.client.paging.IbanityPagingSpec.DEFAULT_PAGING_SPEC;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;
import static org.apache.commons.lang3.StringUtils.removeEnd;

public class IntegrationAccountServiceImpl implements IntegrationAccountService {

    private final IbanityHttpClient ibanityHttpClient;
    private final ApiUrlProvider apiUrlProvider;

    public IntegrationAccountServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        this.ibanityHttpClient = ibanityHttpClient;
        this.apiUrlProvider = apiUrlProvider;
    }

    @Override
    public IbanityCollection<IntegrationAccount> list(IntegrationAccountsReadQuery integrationAccountsReadQuery) {
        IbanityPagingSpec pagingSpec = integrationAccountsReadQuery.getPagingSpec();

        if (pagingSpec == null) {
            pagingSpec = DEFAULT_PAGING_SPEC;
        }

        HttpResponse response = ibanityHttpClient.get(buildUri(getUrl(), pagingSpec), integrationAccountsReadQuery.getAdditionalHeaders(), integrationAccountsReadQuery.getClientAccessToken());
        return IbanityModelMapper.mapCollection(response, customMappingFunction());
    }

    private String getUrl() {
        String url =
                apiUrlProvider
                .find(PontoConnect, "integrationAccounts")
                .replace(IntegrationAccount.API_URL_TAG_ID, "");

        return removeEnd(url, "/");
    }

    private Function<DataApiModel, IntegrationAccount> customMappingFunction() {
        return dataApiModel -> {
            IntegrationAccount integrationAccount = toIbanityModel(dataApiModel, IntegrationAccount.class);

            RelationshipsApiModel financialInstitution = dataApiModel.getRelationships().get("financialInstitution");
            if (financialInstitution != null) {
                integrationAccount.setFinancialInstitutionId(UUID.fromString(financialInstitution.getData().getId()));
            }

            RelationshipsApiModel account = dataApiModel.getRelationships().get("account");
            if (account != null) {
                integrationAccount.setAccountId(UUID.fromString(account.getData().getId()));
            }

            RelationshipsApiModel organization = dataApiModel.getRelationships().get("organization");
            if (organization != null) {
                integrationAccount.setOrganizationId(UUID.fromString(organization.getData().getId()));
            }

            return integrationAccount;
        };
    }
}
