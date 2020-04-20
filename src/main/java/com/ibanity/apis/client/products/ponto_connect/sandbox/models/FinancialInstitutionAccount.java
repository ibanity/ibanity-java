package com.ibanity.apis.client.products.ponto_connect.sandbox.models;

import com.ibanity.apis.client.products.xs2a.models.Account;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FinancialInstitutionAccount extends Account {

    public static final String RESOURCE_TYPE = "financialInstitutionAccount";
    public static final String API_URL_TAG_ID = "{" + RESOURCE_TYPE + URL_PARAMETER_ID_POSTFIX + "}";

    private UUID financialInstitutionUserId;
    private Instant createdAt;
    private Instant updatedAt;

}
