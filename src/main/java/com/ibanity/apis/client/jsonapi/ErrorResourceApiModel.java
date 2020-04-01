package com.ibanity.apis.client.jsonapi;

import lombok.*;

import java.util.Collections;
import java.util.List;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResourceApiModel {

    @Builder.Default
    private List<IbanityErrorApiModel> errors = Collections.emptyList();
}
