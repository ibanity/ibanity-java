package com.ibanity.apis.client.products.xs2a.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.products.xs2a.models.Account;
import com.ibanity.apis.client.products.xs2a.models.FinancialInstitution;
import com.ibanity.apis.client.products.xs2a.models.Holding;
import com.ibanity.apis.client.products.xs2a.models.read.HoldingReadQuery;
import com.ibanity.apis.client.products.xs2a.models.read.HoldingsReadQuery;
import com.ibanity.apis.client.products.xs2a.services.HoldingsService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;

import java.util.UUID;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.mapCollection;
import static com.ibanity.apis.client.mappers.IbanityModelMapper.mapResource;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;

public class HoldingsServiceImpl implements HoldingsService {

    private final IbanityHttpClient ibanityHttpClient;
    private final ApiUrlProvider apiUrlProvider;

    public HoldingsServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public IbanityCollection<Holding> list(HoldingsReadQuery holdingsReadQuery) {
        IbanityPagingSpec pagingSpec = holdingsReadQuery.getPagingSpec();
        if (pagingSpec == null) {
            pagingSpec = IbanityPagingSpec.DEFAULT_PAGING_SPEC;
        }

        String url = getUrl(holdingsReadQuery.getFinancialInstitutionId(), holdingsReadQuery.getAccountId());
        HttpResponse response = ibanityHttpClient.get(buildUri(url, pagingSpec), holdingsReadQuery.getAdditionalHeaders(), holdingsReadQuery.getCustomerAccessToken());

        return mapCollection(response, Holding.class);
    }

    @Override
    public Holding find(HoldingReadQuery holdingReadQuery) {
        String url =
                getUrl(holdingReadQuery.getFinancialInstitutionId(), holdingReadQuery.getAccountId())
                        + "/"
                        + holdingReadQuery.getHoldingId().toString();
        HttpResponse response = ibanityHttpClient.get(buildUri(url), holdingReadQuery.getAdditionalHeaders(), holdingReadQuery.getCustomerAccessToken());
        return mapResource(response, Holding.class);
    }

    private String getUrl(UUID financialInstitutionId, UUID accountId) {
        String url = apiUrlProvider.find(IbanityProduct.Xs2a, "customer", "financialInstitution", "account", "holdings");
        return StringUtils.removeEnd(url
                        .replace(FinancialInstitution.API_URL_TAG_ID, financialInstitutionId.toString())
                        .replace(Account.API_URL_TAG_ID, accountId.toString())
                        .replace(Holding.API_URL_TAG_ID, ""),
                "/");
    }
}
