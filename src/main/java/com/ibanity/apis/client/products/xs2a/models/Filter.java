package com.ibanity.apis.client.products.xs2a.models;

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
