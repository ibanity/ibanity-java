package com.ibanity.apis.client.models;

import com.ibanity.apis.client.models.links.AccountInformationAccessLinks;
import com.ibanity.apis.client.models.links.AccountLinks;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountInformationAccessRequest implements IbanityModel {

    public static final String RESOURCE_TYPE = "accountInformationAccessRequest";
    public static final String API_URL_TAG_ID = "{" + RESOURCE_TYPE + URL_PARAMETER_ID_POSTFIX + "}";

    private UUID id;
    private String selfLink;

    private String consentReference;
    private String redirectUri;
    private String status;
    private String locale;
    private String customerIpAddress;

    @Singular("requestedAccountReferences")
    private List<String> requestedAccountReferences;

    private AccountInformationAccessLinks accountInformationAccessLinks;
    private AccountLinks accountLinks;

}
