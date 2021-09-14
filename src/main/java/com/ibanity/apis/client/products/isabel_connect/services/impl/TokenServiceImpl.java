package com.ibanity.apis.client.products.isabel_connect.services.impl;

import com.ibanity.apis.client.http.OAuthHttpClient;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.isabel_connect.models.AccessToken;
import com.ibanity.apis.client.products.isabel_connect.models.InitialToken;
import com.ibanity.apis.client.products.isabel_connect.models.Token;
import com.ibanity.apis.client.products.isabel_connect.models.TokenQuery;
import com.ibanity.apis.client.products.isabel_connect.models.create.AccessTokenCreateQuery;
import com.ibanity.apis.client.products.isabel_connect.models.create.InitialTokenCreateQuery;
import com.ibanity.apis.client.products.isabel_connect.models.revoke.TokenRevokeQuery;
import com.ibanity.apis.client.products.isabel_connect.services.TokenService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import com.ibanity.apis.client.utils.IbanityUtils;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;

import static com.ibanity.apis.client.utils.URIHelper.buildUri;

public class TokenServiceImpl implements TokenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenServiceImpl.class);

    private final ApiUrlProvider apiUrlProvider;
    private final OAuthHttpClient oAuthHttpClient;

    public TokenServiceImpl(ApiUrlProvider apiUrlProvider, OAuthHttpClient oAuthHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.oAuthHttpClient = oAuthHttpClient;
    }

    @Override
    public void revoke(TokenRevokeQuery tokenRevokeQuery) {
        performTokenRequest(tokenRevokeQuery, InitialToken.class);
    }

    @Override
    public InitialToken create(InitialTokenCreateQuery initialTokenCreateQuery) {
        return performTokenRequest(initialTokenCreateQuery, InitialToken.class);
    }

    @Override
    public AccessToken create(AccessTokenCreateQuery accessTokenCreateQuery) {
        return performTokenRequest(accessTokenCreateQuery, AccessToken.class);
    }

    private <T extends Token> T performTokenRequest(TokenQuery query, Class<T> type) {
        try {
            URI uri = buildUri(getUrl(query.path()));
            HttpResponse response = oAuthHttpClient.post(uri, query.getAdditionalHeaders(), query.requestArguments(), query.getClientSecret());
            return IbanityUtils.objectMapper().readValue(response.getEntity().getContent(), type);
        } catch (IOException e) {
            LOGGER.error("oauth token response invalid", e);
            throw new RuntimeException("The response could not be converted.");
        }
    }

    private String getUrl(String... path) {
        return apiUrlProvider.find(IbanityProduct.IsabelConnect, path);
    }
}
