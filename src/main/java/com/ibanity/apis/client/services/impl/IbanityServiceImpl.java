package com.ibanity.apis.client.services.impl;

import com.ibanity.apis.client.builders.IbanityConfiguration;
import com.ibanity.apis.client.factory.HttpsJwksVerificationKeyResolverFactory;
import com.ibanity.apis.client.factory.JwtConsumerFactory;
import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.http.OAuthHttpClient;
import com.ibanity.apis.client.http.factory.IbanityHttpClientFactory;
import com.ibanity.apis.client.http.factory.OauthHttpClientFactory;
import com.ibanity.apis.client.products.isabel_connect.services.IsabelConnectService;
import com.ibanity.apis.client.products.isabel_connect.services.impl.IsabelConnectServiceImpl;
import com.ibanity.apis.client.products.ponto_connect.services.PontoConnectService;
import com.ibanity.apis.client.products.ponto_connect.services.impl.PontoConnectServiceImpl;
import com.ibanity.apis.client.products.xs2a.services.Xs2aService;
import com.ibanity.apis.client.products.xs2a.services.impl.Xs2aServiceImpl;
import com.ibanity.apis.client.services.ApiUrlProvider;
import com.ibanity.apis.client.services.IbanityService;
import com.ibanity.apis.client.utils.IbanityUtils;
import com.ibanity.apis.client.webhooks.services.WebhooksService;
import com.ibanity.apis.client.webhooks.services.impl.WebhooksServiceImpl;
import org.apache.http.client.HttpClient;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.keys.resolvers.VerificationKeyResolver;

import javax.net.ssl.SSLContext;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class IbanityServiceImpl implements IbanityService {

    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;
    private final Xs2aService xs2aService;
    private final PontoConnectService pontoConnectService;
    private final OAuthHttpClient pontoConnectOAuthHttpClient;
    private final IsabelConnectService isabelConnectService;
    private final OAuthHttpClient isabelConnectOAuthHttpClient;
    private final WebhooksService webhooksService;

    public IbanityServiceImpl(ApiUrlProvider apiUrlProvider,
                              IbanityHttpClient ibanityHttpClient,
                              Xs2aService xs2aService,
                              PontoConnectService pontoConnectService,
                              IsabelConnectService isabelConnectService,
                              OAuthHttpClient pontoConnectOAuthHttpClient,
                              OAuthHttpClient isabelConnectOAuthHttpClient,
                              WebhooksService webhooksService) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
        this.xs2aService = xs2aService;
        this.pontoConnectService = pontoConnectService;
        this.isabelConnectService = isabelConnectService;
        this.pontoConnectOAuthHttpClient = pontoConnectOAuthHttpClient;
        this.isabelConnectOAuthHttpClient = isabelConnectOAuthHttpClient;
        this.webhooksService = webhooksService;
    }

    public IbanityServiceImpl(IbanityConfiguration ibanityConfiguration) {
        String pontoConnectClientId = ibanityConfiguration.getPontoConnectOauth2ClientId();
        String isabelConnectClientId = ibanityConfiguration.getIsabelConnectOauth2ClientId();
        HttpClient httpClient = IbanityUtils.httpClient(ibanityConfiguration);
        SSLContext sslContext = IbanityUtils.getSSLContext(ibanityConfiguration);
        this.ibanityHttpClient = new IbanityHttpClientFactory().create(httpClient, sslContext);
        this.apiUrlProvider = new ApiUrlProviderImpl(ibanityHttpClient, ibanityConfiguration.getApiEndpoint(), ibanityConfiguration.getProxyEndpoint());

        this.xs2aService = new Xs2aServiceImpl(apiUrlProvider, ibanityHttpClient);

        if (isBlank(pontoConnectClientId)) {
            this.pontoConnectOAuthHttpClient = null;
            this.pontoConnectService = null;
        } else {
            this.pontoConnectOAuthHttpClient = new OauthHttpClientFactory().create(pontoConnectClientId, httpClient);
            this.pontoConnectService = new PontoConnectServiceImpl(apiUrlProvider, ibanityHttpClient, pontoConnectOAuthHttpClient);
        }

        if (isBlank(isabelConnectClientId)) {
            this.isabelConnectService = null;
            this.isabelConnectOAuthHttpClient = null;
        } else {
            this.isabelConnectOAuthHttpClient = new OauthHttpClientFactory().create(isabelConnectClientId, httpClient);
            this.isabelConnectService = new IsabelConnectServiceImpl(apiUrlProvider, ibanityHttpClient, isabelConnectOAuthHttpClient);
        }

        VerificationKeyResolver verificationKeyResolver = HttpsJwksVerificationKeyResolverFactory.build(apiUrlProvider, ibanityConfiguration, sslContext);
        JwtConsumer jwtConsumer = JwtConsumerFactory.build(ibanityConfiguration, verificationKeyResolver);

        this.webhooksService = new WebhooksServiceImpl(apiUrlProvider, jwtConsumer);
    }

    @Override
    public ApiUrlProvider apiUrlProvider() {
        return apiUrlProvider;
    }

    @Override
    public IbanityHttpClient ibanityHttpClient() {
        return ibanityHttpClient;
    }

    @Override
    public WebhooksService webhooksService() {
        if (webhooksService == null) {
            throw new IllegalStateException("webhooksSignatureService was not properly initialized. Did you configure applicationId?");
        }

        return webhooksService;
    }

    @Override
    public Xs2aService xs2aService() {
        return xs2aService;
    }

    public OAuthHttpClient pontoConnectOAuthHttpClient() {
        if (pontoConnectOAuthHttpClient == null) {
            throw new IllegalStateException("Ponto-connect OauthHttpClient was not properly initialized. Did you configure pontoConnectOAuth2ClientId?");
        }

        return pontoConnectOAuthHttpClient;
    }

    public OAuthHttpClient isabelConnectOAuthHttpClient() {
        if (isabelConnectOAuthHttpClient == null) {
            throw new IllegalStateException("IsabelConnect OauthHttpClient was not properly initialized. Did you configure isabelConnectOAuth2ClientId?");
        }

        return pontoConnectOAuthHttpClient;
    }

    @Override
    public PontoConnectService pontoConnectService() {
        if (pontoConnectService == null) {
            throw new IllegalStateException("PontoConnectService was not properly initialized. Did you configure pontoConnectOAuth2ClientId?");
        }

        return pontoConnectService;
    }

    @Override
    public IsabelConnectService isabelConnectService() {
        if (isabelConnectService == null) {
            throw new IllegalStateException("IsabelConnectService was not properly initialized. Did you configure isabelConnectOAuth2ClientId?");
        }

        return isabelConnectService;
    }
}
