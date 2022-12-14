package com.ibanity.apis.client.products.xs2a.sandbox.models;

import com.ibanity.apis.client.products.xs2a.models.Transaction;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FinancialInstitutionTransaction extends Transaction {

    public static final String RESOURCE_TYPE = "financialInstitutionTransaction";
    public static final String RESOURCE_PATH = "financial-institution-transactions";
    public static final String API_URL_TAG_ID = "{" + RESOURCE_TYPE + URL_PARAMETER_ID_POSTFIX + "}";

    private UUID financialInstitutionAccountId;
    private String status;
    private boolean automaticBooking;

    private Instant createdAt;
    private Instant updatedAt;

}


