package com.ibanity.apis.client.webhooks.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Key {

    private String alg;
    private String e;
    private String kid;
    private String kty;
    private String n;
    private String status;
    private String use;
}
