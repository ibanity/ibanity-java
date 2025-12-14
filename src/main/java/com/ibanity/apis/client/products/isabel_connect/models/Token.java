package com.ibanity.apis.client.products.isabel_connect.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class Token {

    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("expires_in")
    private int expiresIn;
    @JsonProperty("refresh_expires_in")
    private int refreshExpiresIn;
    private String scope;
}
