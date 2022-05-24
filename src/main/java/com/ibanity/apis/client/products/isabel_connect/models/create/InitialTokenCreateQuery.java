package com.ibanity.apis.client.products.isabel_connect.models.create;

import com.ibanity.apis.client.products.isabel_connect.models.TokenQuery;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.Map;

@Data
@SuperBuilder
public class InitialTokenCreateQuery extends TokenQuery {
    public static final String[] PATH = new String[]{"oAuth2", "token"};

    private String authorizationCode;
    private String redirectUri;

    @Override
    public String[] path() {
        return PATH;
    }

    @Override
    public Map<String, String> requestArguments() {
        Map<String, String> arguments = new HashMap<>();
        arguments.put("grant_type", "authorization_code");
        arguments.put("code", authorizationCode);
        arguments.put("redirect_uri", redirectUri);

        return arguments;
    }
}
