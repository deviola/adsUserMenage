package com.ostasz.ads.user.datamodel.dto.request;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String firstName;
    private String lastName;
    private String pesel;
}
