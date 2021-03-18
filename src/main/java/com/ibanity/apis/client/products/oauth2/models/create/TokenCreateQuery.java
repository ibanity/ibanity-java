package com.ibanity.apis.client.products.oauth2.models.create;

import lombok.*;

import java.util.Map;

import static java.util.Collections.emptyMap;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenCreateQuery {

    private String authorizationCode;
    private String codeVerifier;
    private String redirectUri;
    private String clientSecret;

    @Builder.Default
    private Map<String, String> additionalHeaders = emptyMap();
}
