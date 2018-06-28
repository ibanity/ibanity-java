package com.ibanity.apis.client.sandbox.models;

import com.ibanity.apis.client.models.AbstractModel;
import com.ibanity.apis.client.sandbox.annotations.InstantFormatAnnotation;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.Instant;

public abstract class AbstractTimestamps extends AbstractModel {

    @InstantFormatAnnotation
    private Instant createdAt = null;
    @InstantFormatAnnotation
    private Instant updatedAt = null;
    @InstantFormatAnnotation
    private Instant deletedAt = null;

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(final Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(final Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof AbstractTimestamps)) {
            return false;
        }

        AbstractTimestamps that = (AbstractTimestamps) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(getCreatedAt(), that.getCreatedAt())
                .append(getUpdatedAt(), that.getUpdatedAt())
                .append(getDeletedAt(), that.getDeletedAt())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(getCreatedAt())
                .append(getUpdatedAt())
                .append(getDeletedAt())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(super.toString())
                .append("createdAt", createdAt)
                .append("deletedAt", deletedAt)
                .append("updatedAt", updatedAt)
                .toString();
    }
}
