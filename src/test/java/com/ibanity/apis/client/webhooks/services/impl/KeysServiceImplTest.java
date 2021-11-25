package com.ibanity.apis.client.webhooks.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.services.ApiUrlProvider;
import com.ibanity.apis.client.utils.IbanityUtils;
import com.ibanity.apis.client.webhooks.models.Key;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static com.ibanity.apis.client.helpers.IbanityTestHelper.loadFile;
import static com.ibanity.apis.client.helpers.IbanityTestHelper.loadHttpResponse;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class KeysServiceImplTest {

    private static final String KEYS_ENDPOINT = "http://api.ibanity.com/webhooks/keys";

    @InjectMocks
    private KeysServiceImpl keysService;

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    @Test
    void listKeys() throws IOException {
        when(apiUrlProvider.find("webhooks", "keys")).thenReturn(KEYS_ENDPOINT);
        when(ibanityHttpClient.get(eq(buildUri(KEYS_ENDPOINT)))).thenReturn(loadHttpResponse("certificate/jwks.json"));
        List<Key> actual = keysService.list();
        assertThat(actual).containsExactly(expected());
    }

    private Key expected() throws IOException {
        String jwk = loadFile("certificate/jwk.json");
        return IbanityUtils.objectMapper().readValue(jwk, Key.class);
    }
}
