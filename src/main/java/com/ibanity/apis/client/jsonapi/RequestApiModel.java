package com.ibanity.apis.client.jsonapi;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class RequestApiModel {

    private RequestDataApiModel data;

    @Getter
    @Builder
    @ToString
    @EqualsAndHashCode
    public static class RequestDataApiModel {

        private Object attributes;
        private Object meta;
        private String type;
    }
}
