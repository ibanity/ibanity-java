package com.ibanity.apis.client.products.isabel_connect.models;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Transaction extends IsabelModel<UUID> {
    public static final String RESOURCE_TYPE = "transaction";
    public static final String API_URL_TAG_ID = "{accountId}";

    private BigDecimal amount;
    private String counterpartAccountReference;
    private String counterpartFinancialInstitutionBic;
    private String counterpartName;
    private String endToEndId;
    private String internalId;
    private String remittanceInformation;
    private String remittanceInformationType;
    private String status;

    private LocalDate valueDate;
    private LocalDate executionDate;
}
