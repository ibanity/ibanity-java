package com.ibanity.models;

import io.crnk.core.resource.annotations.JsonApiId;

import java.util.Objects;
import java.util.UUID;

public abstract class AbstractModel {
    @JsonApiId
    protected UUID id;

    public AbstractModel(UUID id) {
        this.id = id;
    }

    public AbstractModel() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractModel)) return false;
        AbstractModel that = (AbstractModel) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "AbstractModel{" +
                "id=" + id +
                '}';
    }
}
