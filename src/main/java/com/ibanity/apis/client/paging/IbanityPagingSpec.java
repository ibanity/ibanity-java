package com.ibanity.apis.client.paging;

import io.crnk.core.queryspec.pagingspec.OffsetLimitPagingSpec;
import io.crnk.core.queryspec.pagingspec.PagingSpec;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.UUID;

public class IbanityPagingSpec extends OffsetLimitPagingSpec implements PagingSpec {

    private UUID before = null;
    private UUID after = null;

    public static final Long LIMIT_DEFAULT = 10L;


    public IbanityPagingSpec(OffsetLimitPagingSpec offsetLimitPagingSpec) {
        super(offsetLimitPagingSpec.getLimit(), offsetLimitPagingSpec.getOffset());
    }

    public IbanityPagingSpec() {
        super();
        setLimit(LIMIT_DEFAULT);
    }

    public IbanityPagingSpec(Long limit, UUID before, UUID after) {
        setLimit(limit);
        this.before = before;
        this.after = after;
    }

    public IbanityPagingSpec(Long offset, Long limit) {
        super(offset, limit);
    }

    public UUID getBefore() {
        return before;
    }

    public void setBefore(UUID before) {
        this.before = before;
    }

    public UUID getAfter() {
        return after;
    }

    public void setAfter(UUID after) {
        this.after = after;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(super.toString())
                .append("after", after)
                .append("before", before)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof IbanityPagingSpec)) return false;

        IbanityPagingSpec that = (IbanityPagingSpec) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(getBefore(), that.getBefore())
                .append(getAfter(), that.getAfter())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(getBefore())
                .append(getAfter())
                .toHashCode();
    }
}