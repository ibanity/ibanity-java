package com.ibanity.apis.client.products.isabel_connect.models;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountReport extends IsabelModel<String> {
    public static final String API_URL_TAG_ID = "{accountReportId}";
    public static final String RESOURCE_TYPE = "accountReport";

    public enum Format {
        CODA, MT940, MT940N, MT940E, MT941, MT942, MT942N, CAMT52, CAMT53, CAMT54
    }

    private List<String> accountReferences = Collections.emptyList();
    private List<AccountReferencesAndCurrencies> accountReferencesAndCurrencies = Collections.emptyList();
    private String fileFormat;
    private String fileName;
    private BigInteger fileSize;
    private String financialInstitutionName;
    private LocalDateTime receivedAt;
    private String fileBankFormatType;

    private String requestId;
}
