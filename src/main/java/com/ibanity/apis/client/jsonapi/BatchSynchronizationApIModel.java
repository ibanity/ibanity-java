package com.ibanity.apis.client.jsonapi;

import lombok.*;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BatchSynchronizationApIModel {

    private Instant createdAt;
    private String resourceType;
    private String status;
    private String[] subtypes;
    private Instant cancelAfter;
    private Instant unlessSynchronizedAfter;
    private Instant updatedAt;
    private List<IbanityErrorApiModel> errors;
}
