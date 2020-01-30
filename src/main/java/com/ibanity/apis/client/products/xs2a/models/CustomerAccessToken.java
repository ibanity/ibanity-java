package com.ibanity.apis.client.products.xs2a.models;

import com.ibanity.apis.client.models.IbanityModel;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerAccessToken implements IbanityModel {

    public static final String RESOURCE_TYPE    = "customerAccessToken";

    private UUID id;
    private String selfLink;
    private String token;
    private String applicationCustomerReference;
    private String requestId;

}
