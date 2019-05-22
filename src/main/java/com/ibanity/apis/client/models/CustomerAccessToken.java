package com.ibanity.apis.client.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAccessToken implements IbanityModel {

    public static final String RESOURCE_TYPE    = "customerAccessToken";

    private UUID id;
    private String selfLink;
    private String token;
    private String applicationCustomerReference;

}
