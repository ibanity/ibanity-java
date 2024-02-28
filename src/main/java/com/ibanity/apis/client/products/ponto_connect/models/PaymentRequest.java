package com.ibanity.apis.client.products.ponto_connect.models;

import com.ibanity.apis.client.models.IbanityModel;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentRequest implements IbanityModel {

    public static final String RESOURCE_TYPE = "paymentRequest";
    public static final String API_URL_TAG_ID = "{" + RESOURCE_TYPE + URL_PARAMETER_ID_POSTFIX + "}";

    private UUID id;
    private String selfLink;
    private String requestId;

    private String remittanceInformationType;
    private String remittanceInformation;
    private String currency;
    private String creditorAccountReferenceType;
    private String creditorAccountReference;
    private String debtorAccountReferenceType;
    private String debtorAccountReference;
    private BigDecimal amount;
    private String endToEndId;
    private LocalDate closedAt;
    private LocalDate signedAt;
    private String redirectLink;
    private String signingLink;
}
