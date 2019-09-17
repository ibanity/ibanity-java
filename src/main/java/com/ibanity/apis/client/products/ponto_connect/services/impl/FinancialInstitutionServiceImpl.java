package com.ibanity.apis.client.products.ponto_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.ponto_connect.models.FinancialInstitution;
import com.ibanity.apis.client.products.ponto_connect.models.read.FinancialInstitutionReadQuery;
import com.ibanity.apis.client.products.ponto_connect.models.read.FinancialInstitutionsReadQuery;
import com.ibanity.apis.client.products.ponto_connect.services.FinancialInstitutionService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.mapCollection;
import static com.ibanity.apis.client.mappers.IbanityModelMapper.mapResource;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;

public class FinancialInstitutionServiceImpl implements FinancialInstitutionService {

    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public FinancialInstitutionServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public FinancialInstitution find(FinancialInstitutionReadQuery financialInstitutionReadQuery) {
        URI uri = buildUri(getUrl()
                + "/"
                + financialInstitutionReadQuery.getFinancialInstitutionId().toString()
        );

        String response = ibanityHttpClient.get(uri, financialInstitutionReadQuery.getAdditionalHeaders(), financialInstitutionReadQuery.getAccessToken());
        return mapResource(response, FinancialInstitution.class);
    }

    @Override
    public IbanityCollection<FinancialInstitution> list(FinancialInstitutionsReadQuery financialInstitutionsReadQuery) {
        String response = ibanityHttpClient.get(buildUri(getUrl()), financialInstitutionsReadQuery.getAdditionalHeaders(), financialInstitutionsReadQuery.getAccessToken());
        return mapCollection(response, FinancialInstitution.class);
    }

    private String getUrl() {
        return StringUtils.removeEnd(
                apiUrlProvider.find(IbanityProduct.PontoConnect, "financialInstitutions")
                        .replace(FinancialInstitution.API_URL_TAG_ID, ""),
                "/");
    }
}
