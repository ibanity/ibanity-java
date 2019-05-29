package com.ibanity.apis.client.paging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IbanityPagingSpec {

    public static final IbanityPagingSpec DEFAULT_PAGING_SPEC = new IbanityPagingSpec();

    @Builder.Default
    private long limit = 10L;
    private UUID before;
    private UUID after;

}
