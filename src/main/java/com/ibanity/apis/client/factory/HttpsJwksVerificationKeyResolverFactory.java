package com.ibanity.apis.client.factory;

import com.ibanity.apis.client.builders.IbanityConfiguration;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.jose4j.http.Get;
import org.jose4j.jwk.HttpsJwks;
import org.jose4j.keys.resolvers.HttpsJwksVerificationKeyResolver;
import org.jose4j.keys.resolvers.VerificationKeyResolver;

import javax.net.ssl.SSLContext;

public class HttpsJwksVerificationKeyResolverFactory {

    public static VerificationKeyResolver build(ApiUrlProvider apiUrlProvider, IbanityConfiguration ibanityConfiguration, SSLContext sslContext) {
        int keysCacheExpirationSeconds = ibanityConfiguration.getWebhooksJwksCacheTTL();

        Get simpleHttpGet = new Get();
        simpleHttpGet.setSslSocketFactory(sslContext.getSocketFactory());

        HttpsJwks httpsJwks = new HttpsJwks(apiUrlProvider.find("webhooks", "keys"));
        httpsJwks.setRefreshReprieveThreshold(keysCacheExpirationSeconds);
        httpsJwks.setSimpleHttpGet(simpleHttpGet);

        return new HttpsJwksVerificationKeyResolver(httpsJwks);
    }
}
