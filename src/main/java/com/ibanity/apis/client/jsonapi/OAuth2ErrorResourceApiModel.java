package com.ibanity.apis.client.jsonapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OAuth2ErrorResourceApiModel {

    private String error;

    @JsonProperty(value = "error_description")
    private String errorDescription;
}
