package com.ibanity.apis.client.products.xs2a.sandbox.models;

import com.ibanity.apis.client.products.xs2a.models.Holding;
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
public class FinancialInstitutionHolding extends Holding {

    public static final String RESOURCE_TYPE = "financialInstitutionHolding";
    public static final String RESOURCE_PATH = "financial-institution-holdings";
    public static final String API_URL_TAG_ID = "{" + RESOURCE_TYPE + URL_PARAMETER_ID_POSTFIX + "}";

    private UUID financialInstitutionAccountId;

    private Instant createdAt;
    private Instant updatedAt;

}
