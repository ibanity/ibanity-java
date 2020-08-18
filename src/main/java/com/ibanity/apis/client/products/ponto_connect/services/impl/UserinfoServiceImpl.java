package com.ibanity.apis.client.products.ponto_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.ponto_connect.models.Userinfo;
import com.ibanity.apis.client.products.ponto_connect.models.read.UserinfoReadQuery;
import com.ibanity.apis.client.products.ponto_connect.services.UserinfoService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import com.ibanity.apis.client.utils.IbanityUtils;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;

import static com.ibanity.apis.client.utils.URIHelper.buildUri;

public class UserinfoServiceImpl implements UserinfoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserinfoServiceImpl.class);

    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public UserinfoServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public Userinfo getUserinfo(UserinfoReadQuery readQuery) {
        try {
            URI uri = buildUri(getUrl());
            HttpResponse response = ibanityHttpClient.get(uri, readQuery.getAdditionalHeaders(), readQuery.getAccessToken());
            return IbanityUtils.objectMapper().readValue(response.getEntity().getContent(), Userinfo.class);
        } catch (IOException e) {
            LOGGER.error("userinfo response invalid", e);
            throw new RuntimeException("The response could not be parsed.");
        }
    }

    private String getUrl() {
        return apiUrlProvider.find(IbanityProduct.PontoConnect, "userinfo");
    }
}
