package com.ibanity.apis.client.products.ponto_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.DataApiModel;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.ponto_connect.models.Account;
import com.ibanity.apis.client.products.ponto_connect.models.ReauthorizationRequest;
import com.ibanity.apis.client.products.ponto_connect.models.create.ReauthorizationRequestCreateQuery;
import com.ibanity.apis.client.products.ponto_connect.services.ReauthorizationRequestService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.UUID;
import java.util.function.Function;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.mapResource;
import static com.ibanity.apis.client.mappers.IbanityModelMapper.toIbanityModel;
import static com.ibanity.apis.client.mappers.ModelMapperHelper.buildRequest;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;

public class ReauthorizationRequestServiceImpl implements ReauthorizationRequestService {

    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public ReauthorizationRequestServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public com.ibanity.apis.client.products.ponto_connect.models.ReauthorizationRequest create(ReauthorizationRequestCreateQuery reauthorizationRequestCreateQuery) {
        URI uri = buildUri(getUrl(reauthorizationRequestCreateQuery.getAccountId()));

        ReauthorizationRequest reauthorizationRequest = toRequest(reauthorizationRequestCreateQuery);
        RequestApiModel requestApiModel = buildRequest(com.ibanity.apis.client.products.ponto_connect.models.ReauthorizationRequest.RESOURCE_TYPE, reauthorizationRequest);
        HttpResponse response = ibanityHttpClient.post(uri, requestApiModel, reauthorizationRequestCreateQuery.getAdditionalHeaders(), reauthorizationRequestCreateQuery.getAccessToken());
        return mapResource(response, customMapping());
    }

    private Function<DataApiModel, com.ibanity.apis.client.products.ponto_connect.models.ReauthorizationRequest> customMapping() {
        return dataApiModel -> {
            com.ibanity.apis.client.products.ponto_connect.models.ReauthorizationRequest reauthorizationRequest = toIbanityModel(dataApiModel, com.ibanity.apis.client.products.ponto_connect.models.ReauthorizationRequest.class);
            reauthorizationRequest.setRedirectLink(dataApiModel.getLinks().getRedirect());

            return reauthorizationRequest;
        };
    }

    private String getUrl(UUID accountId) {
        return StringUtils.removeEnd(
                apiUrlProvider.find(IbanityProduct.PontoConnect, "account", "reauthorizationRequests")
                        .replace(Account.API_URL_TAG_ID, accountId.toString())
                        .replace(com.ibanity.apis.client.products.ponto_connect.models.ReauthorizationRequest.API_URL_TAG_ID, ""),
                "/");
    }

    private ReauthorizationRequest toRequest(ReauthorizationRequestCreateQuery reauthorizationRequestCreateQuery) {
        return ReauthorizationRequest.builder()
                .redirectUri(reauthorizationRequestCreateQuery.getRedirectUri())
                .build();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class ReauthorizationRequest {
        private String redirectUri;
    }
}
