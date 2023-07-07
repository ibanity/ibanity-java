package com.ibanity.apis.client.products.xs2a.models;

import com.ibanity.apis.client.models.IbanityModel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionDeleteRequest implements IbanityModel {

    public static final String RESOURCE_TYPE    = "transactionDeleteRequest";
    public static final String API_URL_TAG_ID   = "{" + RESOURCE_TYPE + URL_PARAMETER_ID_POSTFIX + "}";

    private UUID id;
    private String requestId;

    private Instant beforeDate;
    private String selfLink;
}

