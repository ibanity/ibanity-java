package com.ibanity.apis.client.services.impl;

import com.ibanity.apis.client.builders.IbanityConfiguration;
import com.ibanity.apis.client.exceptions.IbanityRuntimeException;
import com.ibanity.apis.client.factory.JwtConsumerFactory;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.message.BasicHeader;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwk.JsonWebKeySet;
import org.jose4j.jwt.NumericDate;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.resolvers.JwksVerificationKeyResolver;
import org.jose4j.lang.JoseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static com.ibanity.apis.client.helpers.IbanityTestHelper.createHttpResponse;
import static com.ibanity.apis.client.helpers.IbanityTestHelper.loadFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.jose4j.jwa.AlgorithmConstraints.ConstraintType.PERMIT;
import static org.jose4j.jws.AlgorithmIdentifiers.RSA_USING_SHA512;

@ExtendWith({MockitoExtension.class})
public class WebhooksSignatureServiceImplTest {

    private static final String JWT = "eyJhbGciOiJSUzUxMiIsImtpZCI6InZlcnlfc2VjdXJlX2tleV8xIn0.eyJhdWQiOiJhZjYxNzM2NS0xYzNmLTQzNTUtOWM4NC0wOWRiNzc1MTIwNWQiLCJkaWdlc3QiOiJyNDNZbHAxN3NjMWF4NWVybHlHaDZWMnBzWGVEVjBWUWM0eTk3cDRoWVVjaEYvS1pPbDVBcGxuL3ZYSkNZZC9maWpWUUZyL1QrSWpBVWtJWXJlcUx5QT09IiwiZXhwIjoxNjM2NTU0NDY0LCJpYXQiOjE2MzY1NTQ0MDQsImlzcyI6Imh0dHBzOi8vYXBpLmliYW5pdHkubG9jYWxob3N0IiwianRpIjoiOWZkODE3YmQtMTcwMC00YmIxLThhNzktNmYzYTc1OTE3ZmI3In0.NxrkGr0X7bYKuUtrDVyDl5GZpNVlcrVIfk6zEHsO6xYdXx78WFjKaZXwa0pATt2p152Ww4PlT262dE2B5KPvbuMvEs1On327IZPD7pgN8dweUgdZMqYj7t3zNo-qboRZhY1K2PU74Tcp4erBOKQYmffm5KSJzAmVPvHFIJgJslk6caRAPYMnS6vGXcTNRPcyNOcDrSq3YVbLvNMCnuGWRSTDB0OHxhDRXWTxdQ5_YihewenWI0GOE7NsbQqewFjmHWdIOiYcS8lAIeqcPxt4k_1lvRPM2cMCIJJ_aXQ_Kekit9i8ERWcvnLqGKMY7SHRiDq_uxO9ujgK4U5o_f_wMZCzKdqLpgEyQDUoUG6zAkqf4-9cEuHZtPir5zzZIOYvS_nOhOYJVfTQ49rW0Y6oIjqQgQziBfqYcK3l8ILt2eAL7nGoBwmihH37BWSiRwKz2UjqRT7RnAPerMtaWd9kfhdus-3UlxZfDqzrxkJYU9Liehc8BP74jyScyByXDxzlBOvftZ7VIKBFkSimmezDbDI76ob0XMCPXYTlq_8tyhQiAnfpRJJkDvJD-a-mWEzCAX8p8Aqf5dY2iVAkQDxCHcv3hc0GgSAlrCQ8H0rzY4BlbPKMw4RiOvPRCRQtXV8FEickFC_fJMMdads4DpK01QpBXQmJcAekIQA1HrfMniI";
    private static final String AUDIENCE = "af617365-1c3f-4355-9c84-09db7751205d";
    private static final Header SIGNATURE_HEADER = new BasicHeader("Signature", JWT);
    private static final int DATE_FROM_JWT_GENERATION = 1636554460;

    private WebhooksSignatureServiceImpl webhooksSignatureService;

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @BeforeEach
    void setUp() throws IOException, JoseException {
        webhooksSignatureService = new WebhooksSignatureServiceImpl(apiUrlProvider, getJwtConsumer(AUDIENCE, false));
    }

    @Test
    public void verify() throws Exception {
        HttpResponse httpResponse = createHttpResponse(payload(), SIGNATURE_HEADER);
        webhooksSignatureService.verify(httpResponse);
    }

    @Test
    public void verify_invalidDigest() throws Exception {
        HttpResponse httpResponse = createHttpResponse("{temperedPayload}", SIGNATURE_HEADER);

        IbanityRuntimeException thrown = Assertions.assertThrows(IbanityRuntimeException.class, () -> webhooksSignatureService.verify(httpResponse));

        assertThat(thrown.getMessage()).contains("Signature digest value mismatch.");
    }

    @Test
    public void verify_invalidAudience() throws Exception {
        webhooksSignatureService = new WebhooksSignatureServiceImpl(apiUrlProvider, getJwtConsumer("test-audience", false));

        IbanityRuntimeException thrown = Assertions.assertThrows(IbanityRuntimeException.class, () -> webhooksSignatureService.verify(payload(), SIGNATURE_HEADER));

        assertThat(thrown.getMessage()).contains("Audience (aud) claim [af617365-1c3f-4355-9c84-09db7751205d] doesn't contain an acceptable identifier");
    }

    @Test
    public void verify_Expiration() throws Exception {
        webhooksSignatureService = new WebhooksSignatureServiceImpl(apiUrlProvider, getJwtConsumer(AUDIENCE, true));

        IbanityRuntimeException thrown = Assertions.assertThrows(IbanityRuntimeException.class, () -> webhooksSignatureService.verify(payload(), JWT));

        assertThat(thrown.getMessage()).contains("The JWT is no longer valid");
    }

    private JwtConsumer getJwtConsumer(String audience, boolean checkJwtExpiration) throws JoseException, IOException {
        JwksVerificationKeyResolver mockedJwkResolver = getMockedJwkResolver();

        if (checkJwtExpiration) {
            IbanityConfiguration ibanityConfiguration = IbanityConfiguration.builder()
                    .applicationId(audience)
                    .build();
            return JwtConsumerFactory.build(ibanityConfiguration, mockedJwkResolver);
        } else {
            return new JwtConsumerBuilder()
                    .setRequireExpirationTime()
                    .setRequireIssuedAt()
                    .setExpectedAudience(audience)
                    .setVerificationKeyResolver(mockedJwkResolver)
                    .setEvaluationTime(NumericDate.fromSeconds(DATE_FROM_JWT_GENERATION))
                    .setExpectedIssuers(true, "https://api.ibanity.localhost")
                    .setJwsAlgorithmConstraints(new AlgorithmConstraints(PERMIT, RSA_USING_SHA512)).build();
        }
    }

    private JwksVerificationKeyResolver getMockedJwkResolver() throws IOException, JoseException {
        String jwks = loadFile("certificate/jwks.json");

        JsonWebKeySet jsonWebKeySet = new JsonWebKeySet(jwks);

        return new JwksVerificationKeyResolver(jsonWebKeySet.getJsonWebKeys());
    }

    private String payload() throws IOException {
        return loadFile("json/webhooks/payload.json").trim();
    }
}
