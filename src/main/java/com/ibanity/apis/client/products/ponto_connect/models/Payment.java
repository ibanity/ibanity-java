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
public class Payment implements IbanityModel {

    public static final String RESOURCE_TYPE = "payment";
    public static final String API_URL_TAG_ID = "{" + RESOURCE_TYPE + URL_PARAMETER_ID_POSTFIX + "}";

    private UUID id;
    private String selfLink;
    private String requestId;

    private String status;
    private LocalDate requestedExecutionDate;
    private String remittanceInformationType;
    private String remittanceInformation;
    private String currency;
    private String creditorName;
    private String creditorAgentType;
    private String creditorAgent;
    private String creditorAccountReferenceType;
    private String creditorAccountReference;
    private BigDecimal amount;
    private String endToEndId;
    private String redirectLink;

}
