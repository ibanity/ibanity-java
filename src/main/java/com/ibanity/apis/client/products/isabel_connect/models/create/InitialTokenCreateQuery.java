package com.ibanity.apis.client.products.isabel_connect.models.create;

import com.ibanity.apis.client.products.isabel_connect.models.TokenQuery;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

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
        Map<String, String> arguments = newHashMap();
        arguments.put("grant_type", "authorization_code");
        arguments.put("client_id", clientId);
        arguments.put("client_secret", clientSecret);
        arguments.put("code", authorizationCode);

        if (redirectUri != null) {
            arguments.put("redirect_uri", redirectUri);
        }

        return arguments;
    }
}
