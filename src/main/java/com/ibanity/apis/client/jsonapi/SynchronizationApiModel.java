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
public class SynchronizationApiModel {

    private Instant createdAt;
    private String resourceId;
    private String resourceType;
    private Boolean customerOnline;
    private String customerIpAddress;
    private String status;
    private String subtype;
    private Instant updatedAt;
    private List<IbanityErrorApiModel> errors;
}
