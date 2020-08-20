package com.ibanity.apis.client.products.ponto_connect.models;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrganizationUsage {

    public static final String API_URL_TAG_ID = "{month}";
    public static final String API_URL_TAG_ORGANIZATION_ID = "{organizationId}";

    private String id;
    private String requestId;

    private BigDecimal paymentCount;
    private BigDecimal paymentAccountCount;
    private BigDecimal accountCount;
}
