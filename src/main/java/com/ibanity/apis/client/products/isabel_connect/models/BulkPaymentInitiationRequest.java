package com.ibanity.apis.client.products.isabel_connect.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BulkPaymentInitiationRequest extends IsabelModel<String> {
    public static final String RESOURCE_TYPE = "bulkPaymentInitiationRequest";

    private String status;
}
