package com.ibanity.apis.client.products.isabel_connect.models.refresh;

import com.ibanity.apis.client.products.isabel_connect.models.TokenQuery;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

@Data
@SuperBuilder
public class TokenRefreshQuery extends TokenQuery {
    public static final String[] PATH = new String[] {"oAuth2", "accessTokens"};

    private String refreshToken;

    @Override
    public String[] path() {
        return PATH;
    }

    @Override
    public Map<String, String> requestArguments() {
        Map<String, String> arguments = newHashMap();
        arguments.put("grant_type", "refresh_token");
        arguments.put("refresh_token", refreshToken);
        arguments.put("client_id", clientId);
        arguments.put("client_secret", clientSecret);

        return arguments;
    }

}
