package com.ibanity.apis.client.products.ponto_connect.models.create;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

import static java.util.Collections.emptyMap;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class PaymentRequestActivationRequestCreateQuery {

    private String accessToken;
    private String redirectUri;

    @Builder.Default
    private Map<String, String> additionalHeaders = emptyMap();

}
