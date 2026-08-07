package com.ibanity.apis.client.products.isabel_connect.models.delete;

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
public class PaymentNotificationDeleteQuery {
    private String accessToken;
    private String paymentNotificationId;

    @Builder.Default
    private Map<String, String> additionalHeaders = emptyMap();
}
