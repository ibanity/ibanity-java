package com.ibanity.apis.client.products.isabel_connect.models;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Map;

import static java.util.Collections.emptyMap;

@SuperBuilder
@Data
public abstract class TokenQuery {
    protected String clientSecret;

    @Builder.Default
    protected Map<String, String> additionalHeaders = emptyMap();

    public abstract String[] path();

    public abstract Map<String, String> requestArguments();
}
