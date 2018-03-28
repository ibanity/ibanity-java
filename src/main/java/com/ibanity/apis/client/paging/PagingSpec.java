package com.ibanity.apis.client.paging;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.UUID;

public class PagingSpec implements io.crnk.core.queryspec.pagingspec.PagingSpec {

    private Integer limit = new Integer(10);
    private UUID before = null;
    private UUID after = null;


    public PagingSpec() {}

    public PagingSpec(Integer limit, UUID before, UUID after) {
        this.limit = limit;
        this.before = before;
        this.after = after;
    }

    public Integer getLimit() {
        return limit;
    }


    public PagingSpec setLimit(final Integer limit) {
        this.limit = limit;
        return this;
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
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof PagingSpec)) return false;

        PagingSpec that = (PagingSpec) o;

        return new EqualsBuilder()
                .append(getLimit(), that.getLimit())
                .append(getBefore(), that.getBefore())
                .append(getAfter(), that.getAfter())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getLimit())
                .append(getBefore())
                .append(getAfter())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("after", after)
                .append("before", before)
                .append("limit", limit)
                .toString();
    }
}