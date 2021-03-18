package com.ibanity.apis.client.products.ponto_connect.models.revoke;

import lombok.*;

import java.util.Map;

import static java.util.Collections.emptyMap;

/**
 * @deprecated
 * <p>Use {@link com.ibanity.apis.client.products.oauth2.models.revoke.TokenRevokeQuery} instead</p>
 */

@Deprecated
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenRevokeQuery {

    private String token;
    private String clientSecret;

    @Builder.Default
    private Map<String, String> additionalHeaders = emptyMap();
}
