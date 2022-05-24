package com.ibanity.apis.client.products.isabel_connect.services.impl;

import com.ibanity.apis.client.http.OAuthHttpClient;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.isabel_connect.models.create.AccessTokenCreateQuery;
import com.ibanity.apis.client.products.isabel_connect.models.create.InitialTokenCreateQuery;
import com.ibanity.apis.client.products.isabel_connect.models.revoke.TokenRevokeQuery;
import com.ibanity.apis.client.products.isabel_connect.models.AccessToken;
import com.ibanity.apis.client.products.isabel_connect.models.InitialToken;
import com.ibanity.apis.client.products.isabel_connect.models.Token;
import com.ibanity.apis.client.services.ApiUrlProvider;
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

import static com.ibanity.apis.client.helpers.IbanityTestHelper.createHttpResponse;
import static com.ibanity.apis.client.helpers.IbanityTestHelper.loadHttpResponse;
import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TokenServiceImplTest {

    private static final String TOKEN_ENDPOINT = "https://api.ibanity.localhost/isabel-connect/oauth2/token";
    private static final String REVOKE_ENDPOINT = "https://api.ibanity.localhost/isabel-connect/oauth2/revoke";
    private static final String CLIENT_ID = "clientId";
    private static final String CLIENT_SECRET = "thisIsASecret";
    private static final String REDIRECT_URI = "https://fake-tpp.com/isabel-connect-authorization";
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
        when(apiUrlProvider.find(IbanityProduct.IsabelConnect, "oAuth2", "token")).thenReturn(TOKEN_ENDPOINT);
        when(apiUrlProvider.find(IbanityProduct.IsabelConnect, "oAuth2", "revoke")).thenReturn(REVOKE_ENDPOINT);
    }

    @Test
    public void createInitialToken() throws Exception {
        when(oAuthHttpClient.post(eq(new URI(TOKEN_ENDPOINT)), eq(emptyMap()), eq(createInitialTokenArguments()), eq(CLIENT_SECRET)))
                .thenReturn(loadHttpResponse("json/isabel-connect/create_token.json"));

        Token actual = tokenService.create(InitialTokenCreateQuery.builder()
                .clientSecret(CLIENT_SECRET)
                .authorizationCode(AUTHORIZATION_CODE)
                .redirectUri(REDIRECT_URI)
                .build());

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(createExpectedInitialToken());
    }

    @Test
    public void createAccessToken() throws Exception {
        when(oAuthHttpClient.post(eq(new URI(TOKEN_ENDPOINT)), eq(emptyMap()), eq(createAccessTokenArguments()), eq(CLIENT_SECRET)))
                .thenReturn(loadHttpResponse("json/isabel-connect/create_token.json"));

        Token actual = tokenService.create(AccessTokenCreateQuery.builder()
                .clientSecret(CLIENT_SECRET)
                .refreshToken(TOKEN)
                .build());

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(createExpectedAccessToken());
    }

    @Test
    public void revoke() throws Exception {
        when(oAuthHttpClient.post(eq(new URI(REVOKE_ENDPOINT)), eq(emptyMap()), eq(revokeTokenArguments()), eq(CLIENT_SECRET)))
                .thenReturn(createHttpResponse("{}"));

        tokenService.revoke(TokenRevokeQuery.builder()
                .clientSecret(CLIENT_SECRET)
                .token(TOKEN)
                .build());

        verify(oAuthHttpClient).post(eq(new URI(REVOKE_ENDPOINT)), eq(emptyMap()), eq(revokeTokenArguments()), eq(CLIENT_SECRET));
    }

    private Map<String, String> createInitialTokenArguments() {
        Map<String, String> arguments = new HashMap<>();
        arguments.put("grant_type", "authorization_code");
        arguments.put("code", AUTHORIZATION_CODE);
        arguments.put("redirect_uri", REDIRECT_URI);

        return arguments;
    }

    private Map<String, String> createAccessTokenArguments() {
        Map<String, String> arguments = new HashMap<>();
        arguments.put("grant_type", "refresh_token");
        arguments.put("refresh_token", TOKEN);

        return arguments;
    }

    private Map<String, String> revokeTokenArguments() {
        Map<String, String> arguments = new HashMap<>();
        arguments.put("token", TOKEN);

        return arguments;
    }

    private Token createExpectedInitialToken() {
        return InitialToken.builder()
                .accessToken("access_token_1603365408")
                .refreshToken("valid_refresh_token")
                .expiresIn(1799)
                .refreshExpiresIn(1800)
                .scope("AI PI offline_access")
                .tokenType("Bearer")
                .build();
    }

    private Token createExpectedAccessToken() {
        return AccessToken.builder()
                .accessToken("access_token_1603365408")
                .refreshToken("valid_refresh_token")
                .expiresIn(1799)
                .refreshExpiresIn(1800)
                .scope("AI PI offline_access")
                .tokenType("Bearer")
                .build();
    }
}
