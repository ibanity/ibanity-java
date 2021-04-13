package com.ibanity.apis.client.products.isabel_connect.models.create;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.InputStream;
import java.util.Map;

import static java.util.Collections.emptyMap;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BulkPaymentInitiationRequestCreateQuery {
    private String accessToken;
    private InputStream stream;
    private String filename;

    @Builder.Default
    private Map<String, String> additionalHeaders = emptyMap();
}
