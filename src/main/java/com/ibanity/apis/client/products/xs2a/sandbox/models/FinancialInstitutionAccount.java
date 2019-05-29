package com.ibanity.apis.client.products.xs2a.sandbox.models;

import com.ibanity.apis.client.products.xs2a.models.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FinancialInstitutionAccount extends Account {

    public static final String RESOURCE_TYPE = "financialInstitutionAccount";
    public static final String API_URL_TAG_ID = "{" + RESOURCE_TYPE + URL_PARAMETER_ID_POSTFIX + "}";

    private UUID financialInstitutionUserId;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

}
