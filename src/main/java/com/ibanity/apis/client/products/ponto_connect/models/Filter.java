package com.ibanity.apis.client.products.ponto_connect.models;

import lombok.*;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class Filter {

    @NonNull
    private String field;
    private String contains;
    private String eq;
    private String in;

}
