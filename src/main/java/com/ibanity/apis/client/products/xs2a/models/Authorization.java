package com.ibanity.apis.client.products.xs2a.models;

import com.ibanity.apis.client.models.IbanityModel;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
/**
 * @deprecated  Replaced by {@link com.ibanity.apis.client.products.xs2a.models.AccountInformationAccessRequestAuthorization}
 */
@Deprecated
public class Authorization implements IbanityModel {

    public static final String RESOURCE_TYPE = "authorization";
    public static final String API_URL_TAG_ID = "{" + RESOURCE_TYPE + URL_PARAMETER_ID_POSTFIX + "}";

    private UUID id;
    private String selfLink;
    private String requestId;

    private String status;
}



