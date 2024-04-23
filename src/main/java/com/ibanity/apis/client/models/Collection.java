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
public class Collection<T> {

    private String requestId;
    private String afterCursor;
    private String beforeCursor;
    private String firstLink;
    private String nextLink;
    private String previousLink;
    private long pageLimit;
    List<T> items;
}


