package com.ibanity.apis.client.products.xs2a.models;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountInformationAccessRequestMeta {

    private AuthorizationPortal authorizationPortal;
    private BigDecimal requestedPastTransactionDays;
}
