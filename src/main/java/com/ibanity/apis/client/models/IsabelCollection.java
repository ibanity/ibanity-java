package com.ibanity.apis.client.models;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class IsabelCollection<T> {

    private String requestId;
    private Integer pagingOffset;
    private Integer pagingTotal;
    List<T> items;
}