package com.ibanity.apis.client.products.isabel_connect.models;

import com.ibanity.apis.client.models.GenericModel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class IsabelModel<T> implements GenericModel<T> {
    public static String URL_PARAMETER_ID_POSTFIX = "Id";

    private T id;
    private String selfLink;
    private String requestId;
}
