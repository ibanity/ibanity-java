package com.ibanity.apis.client.factory;

import com.ibanity.apis.client.builders.IbanityConfiguration;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.resolvers.VerificationKeyResolver;

import static org.jose4j.jwa.AlgorithmConstraints.ConstraintType.PERMIT;
import static org.jose4j.jws.AlgorithmIdentifiers.RSA_USING_SHA512;

public class JwtConsumerFactory {

    public static JwtConsumer build(IbanityConfiguration ibanityConfiguration, VerificationKeyResolver verificationKeyResolver) {
        return new JwtConsumerBuilder()
                .setAllowedClockSkewInSeconds(ibanityConfiguration.getWebhooksJwtClockSkew())
                .setRequireExpirationTime()
                .setRequireIssuedAt()
                .setExpectedIssuers(true, ibanityConfiguration.getApiEndpoint())
                .setExpectedAudience(ibanityConfiguration.getApplicationId())
                .setVerificationKeyResolver(verificationKeyResolver)
                .setJwsAlgorithmConstraints(new AlgorithmConstraints(PERMIT, RSA_USING_SHA512))
                .build();
    }
}
