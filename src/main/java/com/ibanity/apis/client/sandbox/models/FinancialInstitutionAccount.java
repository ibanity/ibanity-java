package com.ibanity.apis.client.sandbox.models;

import com.ibanity.apis.client.annotations.InstantJsonFormat;
import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.models.Account;
import lombok.Data;

import java.time.Instant;

@Data
public class FinancialInstitutionAccount extends Account {

    public static final String RESOURCE_TYPE    = "financialInstitutionAccount";
    public static final String RESOURCE_PATH    = "financial-institution-accounts";
    public static final String API_URL_TAG_ID   = "{" + RESOURCE_TYPE + IbanityConfiguration.URL_PARAMETER_ID_POSTFIX + "}";

    @InstantJsonFormat
    private Instant createdAt;

    @InstantJsonFormat
    private Instant updatedAt;

    @InstantJsonFormat
    private Instant deletedAt;

}
