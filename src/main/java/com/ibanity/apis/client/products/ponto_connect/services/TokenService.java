package com.ibanity.apis.client.products.ponto_connect.services;

import com.ibanity.apis.client.products.ponto_connect.models.Token;

import java.util.Map;

public interface TokenService {

    void revoke(String token, String clientSecret);

    void revoke(Map<String, String> additionalHeaders, String token, String clientSecret);

    Token create(String authorizationCode, String codeVerifier, String redirectUri, String clientSecret);

    Token create(Map<String, String> additionalHeaders, String authorizationCode, String codeVerifier, String redirectUri, String clientSecret);

    Token refresh(String refreshToken, String redirectUri, String clientSecret);

    Token refresh(Map<String, String> additionalHeaders, String refreshToken, String redirectUri, String clientSecret);
}
