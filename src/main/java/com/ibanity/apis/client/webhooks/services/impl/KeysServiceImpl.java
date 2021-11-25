package com.ibanity.apis.client.webhooks.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.services.ApiUrlProvider;
import com.ibanity.apis.client.utils.IbanityUtils;
import com.ibanity.apis.client.webhooks.models.Key;
import com.ibanity.apis.client.webhooks.services.KeysService;
import lombok.*;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.util.List;

import static com.ibanity.apis.client.mappers.ModelMapperHelper.readResponseContent;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;
import static java.util.Collections.emptyList;

public class KeysServiceImpl implements KeysService {


    private final IbanityHttpClient ibanityHttpClient;
    private final ApiUrlProvider apiUrlProvider;

    public KeysServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        this.ibanityHttpClient = ibanityHttpClient;
        this.apiUrlProvider = apiUrlProvider;
    }

    @Override
    public List<Key> list() {
        HttpResponse httpResponse = ibanityHttpClient.get(buildUri(apiUrlProvider.find("webhooks", "keys")));
        try {
            String payload = readResponseContent(httpResponse.getEntity());
            JwksApi jwksApi = IbanityUtils.objectMapper().readValue(payload, JwksApi.class);
            return jwksApi.getKeys();
        } catch (IOException e) {
            throw new IllegalArgumentException("Response cannot be parsed", e);
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class JwksApi {

        @Builder.Default
        private List<Key> keys = emptyList();
    }
}
