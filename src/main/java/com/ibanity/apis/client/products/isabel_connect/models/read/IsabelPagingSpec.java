package com.ibanity.apis.client.products.isabel_connect.models.read;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IsabelPagingSpec {
    public static final IsabelPagingSpec DEFAULT_PAGING_SPEC = new IsabelPagingSpec();

    @Builder.Default
    private Long size = 10L;
    private Long offset;
    private LocalDate from;
    private LocalDate to;
}
