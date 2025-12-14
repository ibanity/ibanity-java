package com.ibanity.apis.client.products.isabel_connect.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AccessToken extends Token {

    @JsonProperty("access_token")
    private String accessToken;

}
