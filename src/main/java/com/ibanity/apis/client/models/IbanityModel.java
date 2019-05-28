package com.ibanity.apis.client.models;

import java.util.UUID;

public interface IbanityModel {

    String URL_PARAMETER_ID_POSTFIX = "Id";

    void setId(UUID uuid);
    void setSelfLink(String selfLink);
}
