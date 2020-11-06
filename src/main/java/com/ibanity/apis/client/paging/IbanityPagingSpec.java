package com.ibanity.apis.client.paging;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IbanityPagingSpec {

    public static final IbanityPagingSpec DEFAULT_PAGING_SPEC = new IbanityPagingSpec();

    @Builder.Default
    private long limit = 10L;
    private String before;
    private String after;

    public static class IbanityPagingSpecBuilder {

        private long limit = 10L;
        private String before;
        private String after;

        public IbanityPagingSpecBuilder before(String before) {
            this.before = before;
            return this;
        }

        public IbanityPagingSpecBuilder after(String after) {
            this.after = after;
            return this;
        }

        public IbanityPagingSpecBuilder before(UUID before) {
            this.before = before.toString();
            return this;
        }

        public IbanityPagingSpecBuilder after(UUID after) {
            this.after = after.toString();
            return this;
        }
    }
}
