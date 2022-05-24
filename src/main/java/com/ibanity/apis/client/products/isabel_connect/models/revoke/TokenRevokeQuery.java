package com.ibanity.apis.client.products.isabel_connect.models.revoke;

import com.ibanity.apis.client.products.isabel_connect.models.TokenQuery;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.Map;

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
        Map<String, String> arguments = new HashMap<>();
        arguments.put("token", token);

        return arguments;
    }
}
