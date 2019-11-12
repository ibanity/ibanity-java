package com.ibanity.apis.client.products.xs2a.models;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountInformationAccessRequestMeta {

    private AuthorizationPortal authorizationPortal;
}
