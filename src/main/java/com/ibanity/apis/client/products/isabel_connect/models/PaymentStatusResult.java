package com.ibanity.apis.client.products.isabel_connect.models;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class PaymentStatusResult {
    private String body;
    private String notificationStatus;
}
