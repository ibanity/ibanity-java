package com.ibanity.apis.client.products.isabel_connect.models;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@ToString
@EqualsAndHashCode
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountReferencesAndCurrencies {

    private String accountReference;
    private String currency;
}
