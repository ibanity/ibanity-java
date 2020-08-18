package com.ibanity.apis.client.products.ponto_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.ponto_connect.models.Userinfo;
import com.ibanity.apis.client.products.ponto_connect.models.read.UserinfoReadQuery;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static com.ibanity.apis.client.helpers.IbanityTestHelper.loadHttpResponse;
import static java.util.Collections.emptyMap;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserinfoServiceImplTest {

    private static final String ACCESS_TOKEN = "thisIsAnAccessToken";
    private static final String USERINFO_ENDPOINT = "https://api.ibanity.localhost/ponto-connect/userinfo";

    @InjectMocks
    private UserinfoServiceImpl userinfoService;

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    @BeforeEach
    void setUp() {
        when(apiUrlProvider.find(IbanityProduct.PontoConnect, "userinfo")).thenReturn(USERINFO_ENDPOINT);
    }

    @Test
    public void getUserinfo() throws URISyntaxException, IOException {

        when(ibanityHttpClient.get(new URI(USERINFO_ENDPOINT), emptyMap(), ACCESS_TOKEN))
                .thenReturn(loadHttpResponse("json/ponto-connect/userinfo.json"));

        Userinfo actual = userinfoService.getUserinfo(UserinfoReadQuery.builder()
                .accessToken(ACCESS_TOKEN)
                .build());

        Assertions.assertThat(actual).isEqualToComparingFieldByField(expected());
    }

    private Userinfo expected() {
        return Userinfo.builder()
                .name("New documentation")
                .sub("6680437c-8ed8-425b-84b7-2c31e5ca625d")
                .build();
    }


}
