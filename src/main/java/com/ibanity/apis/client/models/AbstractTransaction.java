package com.ibanity.apis.client.models;

import com.ibanity.apis.client.annotations.InstantJsonFormat;
import lombok.Data;

import java.time.Instant;

@Data
public abstract class AbstractTransaction extends BaseModel {

    private Double amount;
    private String currency;

    @InstantJsonFormat
    private Instant valueDate;

    @InstantJsonFormat
    private Instant executionDate;

    private String description;
    private String remittanceInformationType;
    private String remittanceInformation;
    private String counterpartName;
    private String counterpartReference;

}
