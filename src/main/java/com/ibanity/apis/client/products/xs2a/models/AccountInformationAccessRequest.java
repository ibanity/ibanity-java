package com.ibanity.apis.client.products.xs2a.models;

import com.ibanity.apis.client.models.IbanityModel;
import com.ibanity.apis.client.products.xs2a.models.links.AccountInformationAccessLinks;
import com.ibanity.apis.client.products.xs2a.models.links.AccountLinks;
import lombok.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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
    private boolean allowFinancialInstitutionRedirect;

    private Meta meta;

    @Builder.Default
    private List<String> requestedAccountReferences = Collections.emptyList();

    private AccountInformationAccessLinks accountInformationAccessLinks;
    private AccountLinks accountLinks;

}
