package com.ibanity.apis.client.products.ponto_connect.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * @deprecated
 * <p>Use {@link com.ibanity.apis.client.products.oauth2.models.Token} instead</p>
 */

@Deprecated
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Token {

    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("expires_in")
    private int expiresIn;
    private String scope;
}
