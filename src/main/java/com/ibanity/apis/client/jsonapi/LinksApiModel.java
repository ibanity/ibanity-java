package com.ibanity.apis.client.jsonapi;

import lombok.*;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LinksApiModel {

    private String self;
    private String first;
    private String prev;
    private String next;
    private String related;
    private String redirect;
    private String nextRedirect;
}
