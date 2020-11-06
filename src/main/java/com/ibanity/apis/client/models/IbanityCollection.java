package com.ibanity.apis.client.models;

import com.ibanity.apis.client.products.xs2a.models.Synchronization;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class IbanityCollection<T> {

    private String requestId;
    private UUID beforeCursor;
    private UUID afterCursor;
    private String firstLink;
    private String nextLink;
    private String previousLink;
    private String lastLink;
    private long pageLimit;
    private long pageNumber;
    private long pageSize;
    private long totalEntries;
    private long totalPages;
    private Synchronization latestSynchronization;
    List<T> items;
}


