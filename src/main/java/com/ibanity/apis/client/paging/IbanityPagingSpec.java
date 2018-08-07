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

    public static final IbanityPagingSpec DEFAULT_PAGING_SPEC = new IbanityPagingSpec();

    public static final Long DEFAULT_PAGING_SPEC_LIMIT = 10L;


    public IbanityPagingSpec(final OffsetLimitPagingSpec offsetLimitPagingSpec) {
        super(offsetLimitPagingSpec.getLimit(), offsetLimitPagingSpec.getOffset());
    }

    public IbanityPagingSpec() {
        this(DEFAULT_PAGING_SPEC_LIMIT, null, null);
    }

    public IbanityPagingSpec(final Long limit) {
        this(limit,  null, null);
    }

    public IbanityPagingSpec(final Long limit, final UUID before, final UUID after) {
        setLimit(limit);
        this.before = before;
        this.after = after;
    }

    public IbanityPagingSpec(final Long offset, final Long limit) {
        throw new UnsupportedOperationException("This operation is not supported");
    }

    public UUID getBefore() {
        return before;
    }

    public void setBefore(final UUID before) {
        this.before = before;
        this.after = null;
    }

    public UUID getAfter() {
        return after;
    }

    public void setAfter(final UUID after) {
        this.after = after;
        this.before = null;
    }

    @Override
    public OffsetLimitPagingSpec setOffset(final long offset) {
        throw new UnsupportedOperationException("This operation is not supported");
    }

    @Override
    public long getOffset() {
        throw new UnsupportedOperationException("This operation is not supported");
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
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof IbanityPagingSpec)) {
            return false;
        }

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
