package com.ibanity.apis.client.products.isabel_connect.models;

import com.ibanity.apis.client.models.GenericModel;

public interface IsabelModel extends GenericModel<String> {
    String URL_PARAMETER_ID_POSTFIX = "Id";

    void setSelfLink(String selfLink);

    void setRequestId(String requestId);
}
