package com.ibanity.apis.client.products.ponto_connect.services.impl;

import com.ibanity.apis.client.http.OAuthHttpClient;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.ponto_connect.models.Token;
import com.ibanity.apis.client.products.ponto_connect.models.create.TokenCreateQuery;
import com.ibanity.apis.client.products.ponto_connect.models.refresh.TokenRefreshQuery;
import com.ibanity.apis.client.products.ponto_connect.models.revoke.TokenRevokeQuery;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.apache.http.message.BasicHttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static com.ibanity.apis.client.helpers.IbanityTestHelper.HTTP;
import static com.ibanity.apis.client.helpers.IbanityTestHelper.loadHttpResponse;
import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TokenServiceImplTest {

    private static final String TOKEN_ENDPOINT = "https://api.ibanity.localhost/ponto-connect/oauth2/token";
    private static final String REVOKE_ENDPOINT = "https://api.ibanity.localhost/ponto-connect/oauth2/revoke";
    private static final String CLIENT_SECRET = "thisIsASecret";
    private static final String REDIRECT_URI = "https://fake-tpp.com/ponto-authorization";
    private static final String CODE_VERIFIER = "W1qfWXH3sbboERgZFytVIpXOzicKFq1s1-QGQuKzeko.QQRNd24KDohgkrMYleOmDzAGzwMP5PIETP6B267dJ-w_verifier";
    private static final String AUTHORIZATION_CODE = "W1qfWXH3sbboERgZFytVIpXOzicKFq1s1-QGQuKzeko.QQRNd24KDohgkrMYleOmDzAGzwMP5PIETP6B267dJ-w";
    private static final String TOKEN = "I_UnPbrN5wH3-b7fhk5p4GIIL_5c5o-0BhXD4myLDoY.vNxkg6557o3pLCHsweSboi2mmeygG1pXVD6k8M95LLJ";

    @InjectMocks
    private TokenServiceImpl tokenService;

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @Mock
    private OAuthHttpClient oAuthHttpClient;

    @BeforeEach
    void setUp() {
        when(apiUrlProvider.find(IbanityProduct.PontoConnect, "oauth2", "token")).thenReturn(TOKEN_ENDPOINT);
        when(apiUrlProvider.find(IbanityProduct.PontoConnect, "oauth2", "revoke")).thenReturn(REVOKE_ENDPOINT);
    }

    @Test
    public void create() throws Exception {
        when(oAuthHttpClient.post(eq(new URI(TOKEN_ENDPOINT)), eq(emptyMap()), eq(createTokenArguments()), eq(CLIENT_SECRET)))
                .thenReturn(loadHttpResponse("json/ponto-connect/create_access_token.json"));

        Token actual = tokenService.create(TokenCreateQuery.builder()
                .clientSecret(CLIENT_SECRET)
                .redirectUri(REDIRECT_URI)
                .codeVerifier(CODE_VERIFIER)
                .authorizationCode(AUTHORIZATION_CODE)
                .build());

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(createExcepted());
    }

    @Test
    public void refresh() throws Exception {
        when(oAuthHttpClient.post(eq(new URI(TOKEN_ENDPOINT)), eq(emptyMap()), eq(refreshTokenArguments()), eq(CLIENT_SECRET)))
                .thenReturn(loadHttpResponse("json/ponto-connect/create_access_token.json"));

        Token actual = tokenService.refresh(TokenRefreshQuery.builder()
                .clientSecret(CLIENT_SECRET)
                .refreshToken(TOKEN)
                .build());

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(createExcepted());
    }

    @Test
    public void revoke() throws Exception {
        when(oAuthHttpClient.post(any(), any(), any(), any()))
                .thenReturn(new BasicHttpResponse(HTTP, 204, null));

        tokenService.revoke(TokenRevokeQuery.builder()
                .clientSecret(CLIENT_SECRET)
                .token(TOKEN)
                .build());

        verify(oAuthHttpClient).post(eq(new URI(REVOKE_ENDPOINT)), eq(emptyMap()), eq(revokeTokenArguments()), eq(CLIENT_SECRET));
    }

    private Map<String, String> createTokenArguments() {
        Map<String, String> arguments = newHashMap();
        arguments.put("grant_type", "authorization_code");
        arguments.put("code", AUTHORIZATION_CODE);
        arguments.put("code_verifier", CODE_VERIFIER);
        arguments.put("redirect_uri", REDIRECT_URI);
        return arguments;
    }

    private Map<String, String> refreshTokenArguments() {
        Map<String, String> arguments = newHashMap();
        arguments.put("grant_type", "refresh_token");
        arguments.put("refresh_token", TOKEN);
        return arguments;
    }

    private Map<String, String> revokeTokenArguments() {
        Map<String, String> arguments = newHashMap();
        arguments.put("token", TOKEN);
        return arguments;
    }

    private Token createExcepted() {
        return Token.builder()
                .accessToken("MoH3t9zBrnTvlt0qB061Ptjdy0akwwYHm9VY9E6-e4E.a2nK0TfrXsTkVhKzILhKa59_6G1WIUrmvWXko6uYc28")
                .expiresIn(1799)
                .scope("offline_access ai pi")
                .tokenType("bearer")
                .refreshToken("I_UnPbrN5wH3-b7fhk5p4GIIL_5c5o-0BhXD4myLDoY.vNxkg6557o3pLCHsweSboi2mmeygG1pXVD6k8M95LLM")
                .build();
    }
}
