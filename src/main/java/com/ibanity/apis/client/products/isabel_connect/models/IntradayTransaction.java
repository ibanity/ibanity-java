package com.ibanity.apis.client.products.isabel_connect.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IntradayTransaction extends IsabelModel<UUID> {
    public static final String RESOURCE_TYPE = "intradayTransaction";
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
