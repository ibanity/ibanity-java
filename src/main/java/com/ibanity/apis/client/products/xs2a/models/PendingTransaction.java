package com.ibanity.apis.client.products.xs2a.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PendingTransaction extends Transaction {

    public static final String RESOURCE_TYPE = "pendingTransaction";
    public static final String API_URL_TAG_ID = "{" + RESOURCE_TYPE + URL_PARAMETER_ID_POSTFIX + "}";
}

