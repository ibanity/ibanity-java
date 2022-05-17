package com.ibanity.apis.client.jsonapi;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BatchSynchronizationApiModel {

    private Instant createdAt;
    private String resourceType;
    private List<String> subtypes;
    private Instant cancelAfter;
    private Instant unlessSynchronizedAfter;
    private List<IbanityErrorApiModel> errors;
}
