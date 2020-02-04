package com.ibanity.apis.client.products.xs2a.models;

import com.ibanity.apis.client.models.IbanityModel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Holding implements IbanityModel {

    public static final String RESOURCE_TYPE = "holding";
    public static final String API_URL_TAG_ID = "{" + RESOURCE_TYPE + URL_PARAMETER_ID_POSTFIX + "}";

    private UUID id;
    private String selfLink;
    private String requestId;

    private String name;
    private String reference;
    private String referenceType;
    private String subtype;

    private BigDecimal quantity;

    private BigDecimal totalValuation;
    private String totalValuationCurrency;

    private String lastValuationCurrency;
    private BigDecimal lastValuation;
    private LocalDate lastValuationDate;
}
