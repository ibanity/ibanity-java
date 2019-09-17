package com.ibanity.apis.client.products.ponto_connect.services.impl;

import com.ibanity.apis.client.http.OauthHttpClient;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.ponto_connect.models.Token;
import com.ibanity.apis.client.products.ponto_connect.services.TokenService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import com.ibanity.apis.client.utils.IbanityUtils;
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
    private final OauthHttpClient oauthHttpClient;

    public TokenServiceImpl(ApiUrlProvider apiUrlProvider, OauthHttpClient oauthHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.oauthHttpClient = oauthHttpClient;
    }

    @Override
    public void revoke(String token, String clientSecret) {
        revoke(newHashMap(), token, clientSecret);
    }

    @Override
    public void revoke(Map<String, String> additionalHeaders, String token, String clientSecret) {
        URI uri = buildUri(getUrl("revoke"));
        oauthHttpClient.post(uri, additionalHeaders, getDeleteTokenRequestArguments(token), clientSecret);
    }

    @Override
    public Token create(String authorizationCode, String codeVerifier, String redirectUri, String clientSecret) {
        return create(newHashMap(), authorizationCode, codeVerifier, redirectUri, clientSecret);
    }

    @Override
    public Token create(Map<String, String> additionalHeaders, String authorizationCode, String codeVerifier, String redirectUri, String clientSecret) {
        Map<String, String> accessTokenRequestArguments = getAccessTokenRequestArguments(authorizationCode, codeVerifier, redirectUri);
        return performTokenRequest(accessTokenRequestArguments, clientSecret, additionalHeaders);
    }

    @Override
    public Token refresh(String refreshToken, String redirectUri, String clientSecret) {
        return refresh(newHashMap(), refreshToken, redirectUri, clientSecret);
    }

    @Override
    public Token refresh(Map<String, String> additionalHeaders, String refreshToken, String redirectUri, String clientSecret) {
        Map<String, String> refreshTokenRequestArguments = getRefreshTokenRequestArguments(refreshToken, redirectUri);
        return performTokenRequest(refreshTokenRequestArguments, clientSecret, additionalHeaders);
    }

    private Token performTokenRequest(Map<String, String> refreshTokenRequestArguments, String clientSecret, Map<String, String> additionalHeaders) {
        try {
            URI uri = buildUri(getUrl("token"));
            String response = oauthHttpClient.post(uri, additionalHeaders, refreshTokenRequestArguments, clientSecret);
            return IbanityUtils.objectMapper().readValue(response, Token.class);
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
        arguments.put("grant_type", "authorization_code");
        arguments.put("code", authorizationCode);
        arguments.put("code_verifier", codeVerifier);
        arguments.put("redirect_uri", redirectUri);
        return arguments;
    }

    private Map<String, String> getRefreshTokenRequestArguments(String refreshToken, String redirectUri) {
        Map<String, String> arguments = newHashMap();
        arguments.put("refresh_token", refreshToken);
        arguments.put("redirect_uri", redirectUri);
        arguments.put("grant_type", "refresh_token");
        return arguments;
    }
}
