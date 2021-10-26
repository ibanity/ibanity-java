package com.ibanity.apis.client.products.ponto_connect.models;

import com.ibanity.apis.client.models.IbanityModel;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Transaction implements IbanityModel {

    public static final String RESOURCE_TYPE = "transaction";
    public static final String API_URL_TAG_ID = "{" + RESOURCE_TYPE + URL_PARAMETER_ID_POSTFIX + "}";

    private UUID id;
    private String selfLink;
    private String requestId;

    private BigDecimal amount;
    private String currency;

    private Instant valueDate;
    private Instant executionDate;

    private String description;
    private String remittanceInformationType;
    private String remittanceInformation;
    private String counterpartName;
    private String counterpartReference;

    private String bankTransactionCode;
    private String proprietaryBankTransactionCode;
    private String endToEndId;
    private String purposeCode;
    private String mandateId;
    private String creditorId;
    private String additionalInformation;
    private String digest;
    private String internalReference;
    private BigDecimal fee;
    private String cardReference;
    private String cardReferenceType;
}
