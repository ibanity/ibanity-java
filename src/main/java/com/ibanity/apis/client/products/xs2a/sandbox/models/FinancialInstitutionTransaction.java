package com.ibanity.apis.client.products.xs2a.sandbox.models;

import com.ibanity.apis.client.products.xs2a.models.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FinancialInstitutionTransaction extends Transaction {

    public static final String RESOURCE_TYPE = "financialInstitutionTransaction";
    public static final String RESOURCE_PATH = "financial-institution-transactions";
    public static final String API_URL_TAG_ID = "{" + RESOURCE_TYPE + URL_PARAMETER_ID_POSTFIX + "}";

    private UUID financialInstitutionAccountId;

    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

}


