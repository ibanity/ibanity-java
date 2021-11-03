package com.ibanity.apis.client.products.isabel_connect.models;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BulkPaymentInitiationRequest extends IsabelModel<String> {
    public static final String RESOURCE_TYPE = "bulkPaymentInitiationRequest";

    private String status;
}
