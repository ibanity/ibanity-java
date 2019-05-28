package com.ibanity.apis.client.sandbox.models;

import com.ibanity.apis.client.models.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder(builderMethodName = "financialInstitutionTransaction")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FinancialInstitutionTransaction extends Transaction {

    public static final String RESOURCE_TYPE = "financialInstitutionTransaction";
    public static final String RESOURCE_PATH = "financial-institution-transactions";
    public static final String API_URL_TAG_ID = "{" + RESOURCE_TYPE + URL_PARAMETER_ID_POSTFIX + "}";

    private Instant createdAt = null;
    private Instant updatedAt = null;
    private Instant deletedAt = null;

}


