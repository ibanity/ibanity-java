package com.ibanity.apis.client.jsonapi;

import com.ibanity.apis.client.models.IbanityError;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResourceApiModel {

    @Singular
    private List<IbanityError> errors;
}
