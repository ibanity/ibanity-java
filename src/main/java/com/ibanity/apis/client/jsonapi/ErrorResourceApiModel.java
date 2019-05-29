package com.ibanity.apis.client.jsonapi;

import com.ibanity.apis.client.models.IbanityError;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResourceApiModel {

    @Builder.Default
    private List<IbanityError> errors = Collections.emptyList();
}
