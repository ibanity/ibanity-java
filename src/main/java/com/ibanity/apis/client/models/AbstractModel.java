package com.ibanity.apis.client.models;

import io.crnk.core.resource.annotations.JsonApiId;
import lombok.Data;

import java.util.UUID;

@Data
public abstract class AbstractModel {
    @JsonApiId
    private UUID id;

}
