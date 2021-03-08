package com.ibanity.apis.client.models;

public interface GenericModel<T> {
    void setId(T t);

    void setSelfLink(String selfLink);

    void setRequestId(String requestId);
}
