package com.ibanity.apis.client.products.ponto_connect.models.refresh;

import lombok.*;

import java.util.Map;

import static java.util.Collections.emptyMap;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenRefreshQuery {

    private String refreshToken;
    private String clientSecret;

    @Builder.Default
    private Map<String, String> additionalHeaders = emptyMap();
}
