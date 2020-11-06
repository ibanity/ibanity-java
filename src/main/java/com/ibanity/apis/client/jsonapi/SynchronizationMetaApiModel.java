package com.ibanity.apis.client.jsonapi;

import lombok.*;

import java.time.Instant;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SynchronizationMetaApiModel {

    private String availability;
    private Instant synchronizedAt;
    private DataApiModel latestSynchronization;
}
