package com.ibanity.apis.client.products.ponto_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.ponto_connect.models.Integration;
import com.ibanity.apis.client.products.ponto_connect.models.delete.OrganizationIntegrationDeleteQuery;
import com.ibanity.apis.client.products.ponto_connect.services.IntegrationService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.apache.http.HttpResponse;

import java.net.URI;
import java.util.UUID;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.mapResource;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;

public class IntegrationServiceImpl implements IntegrationService {

    private ApiUrlProvider apiUrlProvider;
    private IbanityHttpClient ibanityHttpClient;

    public IntegrationServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public Integration delete(OrganizationIntegrationDeleteQuery organizationIntegrationDeleteQuery) {
        URI uri = buildUri(getUrl(organizationIntegrationDeleteQuery.getOrganizationId()));
        HttpResponse response = ibanityHttpClient.delete(uri, organizationIntegrationDeleteQuery.getAdditionalHeaders(), organizationIntegrationDeleteQuery.getAccessToken());
        return mapResource(response, Integration.class);
    }

    private String getUrl(UUID organizationId) {
        return apiUrlProvider.find(IbanityProduct.PontoConnect, "organizations", "integration")
                .replace("{organizationId}", organizationId.toString());
    }

}
