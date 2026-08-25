package com.ibanity.apis.client.products.isabel_connect.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentNotification extends IsabelModel<String> {
    public static final String RESOURCE_TYPE = "paymentNotification";

    private String notificationType;
    private Instant createdAt;
    private String paymentId;
}
