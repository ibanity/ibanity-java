package com.ibanity.apis.client.products.isabel_connect.services.impl;

import com.ibanity.apis.client.http.OAuthHttpClient;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.isabel_connect.models.Token;
import com.ibanity.apis.client.products.isabel_connect.models.create.TokenCreateQuery;
import com.ibanity.apis.client.products.isabel_connect.models.refresh.TokenRefreshQuery;
import com.ibanity.apis.client.products.isabel_connect.models.revoke.TokenRevokeQuery;
import com.ibanity.apis.client.products.isabel_connect.services.TokenService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import com.ibanity.apis.client.utils.IbanityUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
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
        URI uri = buildUri(getUrl("revoke"));
        Map<String, String> deleteTokenRequestArguments = getDeleteTokenRequestArguments(tokenRevokeQuery.getToken());
        oAuthHttpClient.post(uri, tokenRevokeQuery.getAdditionalHeaders(), deleteTokenRequestArguments, tokenRevokeQuery.getClientSecret());
    }

    @Override
    public Token create(TokenCreateQuery tokenCreateQuery) {
        Map<String, String> accessTokenRequestArguments = getAccessTokenRequestArguments(
                tokenCreateQuery.getAuthorizationCode(),
                tokenCreateQuery.getCodeVerifier(),
                tokenCreateQuery.getRedirectUri());
        return performTokenRequest(accessTokenRequestArguments, tokenCreateQuery.getClientSecret(), tokenCreateQuery.getAdditionalHeaders());
    }

    @Override
    public Token refresh(TokenRefreshQuery tokenRefreshQuery) {
        Map<String, String> refreshTokenRequestArguments = getRefreshTokenRequestArguments(tokenRefreshQuery.getRefreshToken());
        return performTokenRequest(refreshTokenRequestArguments, tokenRefreshQuery.getClientSecret(), tokenRefreshQuery.getAdditionalHeaders());
    }

    private Token performTokenRequest(Map<String, String> tokenRequestArguments, String clientSecret, Map<String, String> additionalHeaders) {
        try {
            URI uri = buildUri(getUrl("token"));
            HttpResponse response = oAuthHttpClient.post(uri, additionalHeaders, tokenRequestArguments, clientSecret);
            return IbanityUtils.objectMapper().readValue(response.getEntity().getContent(), Token.class);
        } catch (IOException e) {
            LOGGER.error("oauth token response invalid", e);
            throw new RuntimeException("The response could not be converted.");
        }
    }

    private String getUrl(String path) {
        return apiUrlProvider.find(IbanityProduct.PontoConnect, "oauth2", path);
    }

    private Map<String, String> getDeleteTokenRequestArguments(String token) {
        Map<String, String> arguments = newHashMap();
        arguments.put("token", token);
        return arguments;
    }

    private Map<String, String> getAccessTokenRequestArguments(String authorizationCode, String codeVerifier, String redirectUri) {
        Map<String, String> arguments = newHashMap();
        if (StringUtils.isBlank(authorizationCode)) {
            arguments.put("grant_type", "client_credentials");
        } else {
            arguments.put("grant_type", "authorization_code");
            arguments.put("code", authorizationCode);
            arguments.put("code_verifier", codeVerifier);
            arguments.put("redirect_uri", redirectUri);
        }
        return arguments;
    }

    private Map<String, String> getRefreshTokenRequestArguments(String refreshToken) {
        Map<String, String> arguments = newHashMap();
        arguments.put("refresh_token", refreshToken);
        arguments.put("grant_type", "refresh_token");
        return arguments;
    }
}
