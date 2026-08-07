package com.ibanity.apis.client.products.isabel_connect.models.read;

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
public class PaymentStatusReadQuery {
    private String accessToken;
    private String bulkPaymentInitiationRequestId;
    private String notificationId;

    @Builder.Default
    private Map<String, String> additionalHeaders = emptyMap();
}
