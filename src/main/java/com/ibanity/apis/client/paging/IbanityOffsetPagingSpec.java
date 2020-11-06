package com.ibanity.apis.client.paging;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IbanityOffsetPagingSpec {

    public static final IbanityOffsetPagingSpec DEFAULT_OFFSET_PAGING_SPEC = new IbanityOffsetPagingSpec();

    @Builder.Default
    private long pageSize = 10;
    private long pageNumber;
}
