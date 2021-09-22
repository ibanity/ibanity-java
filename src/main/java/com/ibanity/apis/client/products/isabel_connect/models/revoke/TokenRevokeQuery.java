package com.ibanity.apis.client.products.isabel_connect.models.revoke;

import com.ibanity.apis.client.products.isabel_connect.models.TokenQuery;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

@Data
@SuperBuilder
public class TokenRevokeQuery extends TokenQuery {
    public static final String[] PATH = new String[]{"oAuth2", "revoke"};

    private String token;

    @Override
    public String[] path() {
        return PATH;
    }

    @Override
    public Map<String, String> requestArguments() {
        Map<String, String> arguments = newHashMap();
        arguments.put("token", token);
        arguments.put("client_id", clientId);
        arguments.put("client_secret", clientSecret);

        return arguments;
    }
}
