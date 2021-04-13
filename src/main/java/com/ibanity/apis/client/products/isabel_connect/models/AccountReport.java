package com.ibanity.apis.client.products.isabel_connect.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountReport {
    public static final String API_URL_TAG_ID = "{accountReportId}";
    public static final String RESOURCE_TYPE = "accountReport";

    public enum Format {
        CODA, MT940, MT940N, MT940E, MT941, MT942, MT942N, CAMT52, CAMT53, CAMT54
    }

    private List<String> accountReferences = Collections.emptyList();
    private String fileFormat;
    private String fileName;
    private BigInteger fileSize;
    private String financialInstitutionName;
    private Instant receivedAt;

    private String requestId;
}
