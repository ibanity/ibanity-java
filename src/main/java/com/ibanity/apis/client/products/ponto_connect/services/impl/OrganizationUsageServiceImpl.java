package com.ibanity.apis.client.products.ponto_connect.services.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.ponto_connect.models.OrganizationUsage;
import com.ibanity.apis.client.products.ponto_connect.models.read.OrganizationUsageReadQuery;
import com.ibanity.apis.client.products.ponto_connect.services.OrganizationUsageService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import com.ibanity.apis.client.utils.IbanityUtils;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.util.UUID;

public class OrganizationUsageServiceImpl implements OrganizationUsageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationUsageServiceImpl.class);

    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public OrganizationUsageServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public OrganizationUsage getUsage(OrganizationUsageReadQuery readQuery) {
        try {
            String url = getUrl(readQuery.getOrganizationId(), readQuery.getMonth());
            HttpResponse response = ibanityHttpClient.get(URI.create(url), readQuery.getAdditionalHeaders(), readQuery.getAccessToken());
            JsonNode dataApiModel = IbanityUtils.objectMapper().readTree(response.getEntity().getContent());
            return map(dataApiModel);
        } catch (IOException e) {
            LOGGER.error("OrganizationUsage response invalid", e);
            throw new RuntimeException("The response could not be parsed.", e);
        }
    }

    private OrganizationUsage map(JsonNode dataApiModel) {
        return OrganizationUsage.builder()
                .id(dataApiModel.get("data").get("id").textValue())
                .paymentCount(new BigDecimal(dataApiModel.get("data").get("attributes").get("paymentCount").toString()))
                .accountCount(new BigDecimal(dataApiModel.get("data").get("attributes").get("accountCount").toString()))
                .paymentAccountCount(new BigDecimal(dataApiModel.get("data").get("attributes").get("paymentAccountCount").toString()))
                .build();
    }

    private String getUrl(UUID organizationId, String month) {
        return apiUrlProvider.find(IbanityProduct.PontoConnect, "organizations", "usage")
                .replace(OrganizationUsage.API_URL_TAG_ORGANIZATION_ID, organizationId.toString())
                .replace(OrganizationUsage.API_URL_TAG_ID, month);
    }
}
